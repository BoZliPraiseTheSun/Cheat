package com.example.cheat.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat.*
import com.example.cheat.Adapter.MyAdapterFoodsEaten
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_user.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class UserActivity : AppCompatActivity() {

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var fitnessOptions: FitnessOptions
    private lateinit var mAdapter: MyAdapterFoodsEaten

    private val eatenFoods: ArrayList<FoodsEaten> = arrayListOf()
    private val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mSettings = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)

        initializationAdapter()

        checkNextDay()

        buildFitnessOptions()
        AccountGoogle().singInGoogleAccount(this, this)

        getFoodsEaten()

        if (mSettings.getInt(getString(R.string.cal_per_day_key), -1) == -1) {
            mSettings.edit().putInt(getString(R.string.cal_per_day_key), 1250).apply()
        }

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ProductsStoreActivity::class.java)
            startActivity(intent)
        }


        list_eat_recycler.layoutManager = layoutManager
        list_eat_recycler.adapter = mAdapter
    }


    private fun initializationAdapter() {
        mAdapter = MyAdapterFoodsEaten(eatenFoods) { foodEaten ->
            Log.d(TAG, "$foodEaten")
        }
    }

    private fun buildFitnessOptions() {
        fitnessOptions = FitnessOptions
            .builder()
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_BASAL_METABOLIC_RATE, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_BASAL_METABOLIC_RATE_SUMMARY, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_ACTIVITY_SUMMARY, FitnessOptions.ACCESS_READ)
            .build()
    }


    private fun checkNextDay() {
        if (mSettings.getInt(getString(R.string.this_day_key), -1) != getData()) {
            val calBurnAll = mSettings.getInt(getString(R.string.cal_burn_all_key), 0)
            val calBurn = mSettings.getInt(getString(R.string.cal_burn_key), 0)
            val edit = mSettings.edit()
            edit.putInt(getString(R.string.this_day_key),
                getData()
            )
            edit.putInt(getString(R.string.cal_burn_all_key), calBurnAll + calBurn)
            edit.putInt(getString(R.string.cal_eat_key), 0)
            edit.putInt(getString(R.string.cal_burn_key), 0)
            edit.putString(getString(R.string.list_product_eat_key), "")
            edit.apply()
            eatenFoods.clear()
        }
    }

    private fun getFoodsEaten() {
        val gsonText = mSettings.getString(getString(R.string.list_product_eat_key), "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodsEaten>>() {}.type
            eatenFoods.addAll(Gson().fromJson(gsonText, type))
        }
    }


    private fun accessGoogleFit() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val endTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val startTime = calendar.timeInMillis

        val builder = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
            .bucketByActivityType(1, TimeUnit.MILLISECONDS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .readData(builder)
            .addOnSuccessListener {
                Log.d(TAG, "accessGoogle Success")
            }
            .addOnFailureListener {
                Log.d(TAG, "accessGoogle Failure")
            }
            .addOnCompleteListener {
                Log.d(TAG, "accessGoogle Complete")
                var burnCal = 0f
                val buckets = it.result!!.buckets
                for (bucket in buckets) {
                    val activityName = bucket.activity.toString()
                    val dataSets = bucket.dataSets
                    if (activityName != "still" &&
                        activityName != "unknown" &&
                        activityName != "in_vehicle") {
                        for (dataSet in dataSets) {
                            Log.d(TAG, "dataSet ${dataSet.dataType}")
                            val dataPoints = dataSet.dataPoints
                            for (dataPoint in dataPoints) {
                                val avg = dataPoint.getValue(Field.FIELD_CALORIES).asFloat()
                                burnCal += avg
                            }
                        }
                    }
                }
                mSettings.edit().putInt(getString(R.string.cal_burn_key), burnCal.roundToInt()).apply()
            }
    }


    private fun reLoad() {
        val calPerDay = mSettings.getInt(getString(R.string.cal_per_day_key), 0)
        val calBurn = mSettings.getInt(getString(R.string.cal_burn_key), 0)
        val calBurnAll = mSettings.getInt(getString(R.string.cal_burn_all_key), 0)
        val calEat = mSettings.getInt(getString(R.string.cal_eat_key), 0)

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        burn_cal.text = calBurn.toString()
        coin.text = (calBurnAll + calBurn).toString()

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calEat).toString()
        if (cal_left_num_text.text.toString().toInt() < 0) {
            cal_left_num_text.text = "0"
        }

        if (eatenFoods.isNotEmpty()) {
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun caloriesToCoins(cal: Int): Int {
        return (cal * 0.25f).roundToInt()
    }

    private fun setEatenFoods(list: ArrayList<FoodsEaten>, keyForPreferences: String) {
        if (list.isNotEmpty()) {
            val gsonText = Gson().toJson(list)
            mSettings.edit().putString(keyForPreferences, gsonText).apply()
        }
    }

    fun setBurnCaloriesInSettings() {
        val burnCal = BurnCaloriesIvGoogleFit().getBurnCaloriesForOneDay(this)
        mSettings
            .edit()
            .putInt(getString(R.string.cal_burn_key), burnCal.roundToInt())
            .apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> {
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setBurnCaloriesInSettings()
        reLoad()
    }

    override fun onStop() {
        super.onStop()
        setEatenFoods(
            eatenFoods, getString(
                R.string.list_product_eat_key
            ))
    }
}

