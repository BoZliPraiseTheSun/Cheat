package com.example.cheat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_user.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.roundToInt

class UserActivity : AppCompatActivity() {

    companion object {
        const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 77
        val listFoodsEaten: ArrayList<FoodsEaten> = arrayListOf()
    }

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var fitnessOptions: FitnessOptions

    private val dataFormatHHmm = SimpleDateFormat("HH:mm", Locale.UK)
    private val dataFormatDD = SimpleDateFormat("dd", Locale.UK)
    private val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        initializationLateInitParam()
        singInGoogleAccount()

        if (mSettings.getInt(getString(R.string.cal_per_day_key), -1) == -1) {
            mSettings.edit().putInt(getString(R.string.cal_per_day_key), 1250).apply()
        }

        list_eat_recycler.layoutManager = layoutManager

        nextDay()
        getListEat()

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ListProductsActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initializationLateInitParam() {
        mSettings = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)
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


    private fun singInGoogleAccount() {
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
        }
    }


    private fun nextDay() {
        if (mSettings.getInt(getString(R.string.this_day_key), -1) != getData()) {
            val edit = mSettings.edit()
            edit.putInt(getString(R.string.this_day_key), getData())
            edit.putInt(getString(R.string.cal_eat_key), 0)
            edit.putInt(getString(R.string.cal_burn_key), 0)
            edit.putString(getString(R.string.list_product_eat_key), "")
            edit.apply()
            listFoodsEaten.clear()
        }
    }


    private fun getListEat() {
        val gsonText = mSettings.getString(getString(R.string.list_product_eat_key), "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodsEaten>>() {}.type
            listFoodsEaten.clear()
            listFoodsEaten.addAll(Gson().fromJson(gsonText, type))
        }
    }


    private fun accessGoogleFit() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val endTime = calendar.timeInMillis
        Log.d(TAG, "$calendar")
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        Log.d(TAG, "$calendar")
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
                    Log.d(
                        TAG,
                        "bucket ${bucket.activity} ${getData()} ${dataFormatHHmm.format(
                            bucket.getStartTime(TimeUnit.MILLISECONDS)
                        )} - ${dataFormatHHmm.format(bucket.getEndTime(TimeUnit.MILLISECONDS))}"
                    )
                    val activityName = bucket.activity.toString()
                    val dataSets = bucket.dataSets
                    if (activityName != "still" && activityName != "unknown") {
                        for (dataSet in dataSets) {
                            Log.d(TAG, "dataSet ${dataSet.dataType}")
                            val dataPoints = dataSet.dataPoints
                            for (dataPoint in dataPoints) {
                                Log.d(TAG, "dataPoint ${dataPoint.dataType}")
                                Log.d(TAG, "Kkal ${dataPoint.getValue(Field.FIELD_CALORIES)}")
                                val avg = dataPoint.getValue(Field.FIELD_CALORIES).asFloat()
                                burnCal += round(avg)
                            }
                        }
                    }
                }
                Log.d(TAG, "AVG $burnCal")
                mSettings.edit().putInt(getString(R.string.cal_burn_key), burnCal.roundToInt()).apply()
            }
    }

    private fun getData(): Int {
        return dataFormatDD.format(Date()).toInt()
    }


    private fun reLoad() {
        val calPerDay = mSettings.getInt(getString(R.string.cal_per_day_key), 0)
        val calBurn = mSettings.getInt(getString(R.string.cal_burn_key), 0)
        val calEat = mSettings.getInt(getString(R.string.cal_eat_key), 0)

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        burn_cal.text = calBurn.toString()

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calEat).toString()
        if (cal_left_num_text.text.toString().toInt() < 0) {
            cal_left_num_text.text = "0"
        }

        if (listFoodsEaten.isNotEmpty()) {
            list_eat_recycler.adapter = MyAdapterFoodsEaten(listFoodsEaten)
        }
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
        accessGoogleFit()
        reLoad()
    }

    override fun onPause() {
        super.onPause()
        if (listFoodsEaten.isNotEmpty()) {
            val gsonText = Gson().toJson(listFoodsEaten)
            mSettings.edit().putString(getString(R.string.list_product_eat_key), gsonText).apply()
        }
    }
}

