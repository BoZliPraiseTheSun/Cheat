package com.example.cheat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
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
import kotlinx.android.synthetic.main.list_product_eat_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.roundToInt

class UserActivity : AppCompatActivity() {

    companion object {
        const val SETTINGS = "mSettings"
        const val SETTINGS_CAL_PER_FAY = "calPerDay"
        const val SETTINGS_CAL_EAT = "calEat"
        const val SETTINGS_CAL_BURN = "calBurn"
        const val SETTINGS_LIST_EAT_PRODUCTS = "listEatProduct"
        const val SETTINGS_LIST_PRODUCTS = "listProduct"
        const val SETTINGS_THIS_DAY = "thisDay"
        const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 77
        val listEat: ArrayList<ProductEat> = arrayListOf()
    }

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var fitnessOptions: FitnessOptions

    private val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        initializationLateInitParam()
        singInGoogleAccount()

        if (mSettings.getInt(SETTINGS_CAL_PER_FAY, -1) == -1) {
            mSettings.edit().putInt(SETTINGS_CAL_PER_FAY, 1250).apply()
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
        mSettings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
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
        if (mSettings.getInt(SETTINGS_THIS_DAY, -1) != getData()) {
            val edit = mSettings.edit()
            edit.putInt(SETTINGS_THIS_DAY, getData())
            edit.putInt(SETTINGS_CAL_EAT, 0)
            edit.putInt(SETTINGS_CAL_BURN, 0)
            edit.putString(SETTINGS_LIST_EAT_PRODUCTS, "")
            edit.apply()
            listEat.clear()
        }
    }


    private fun getListEat() {
        val gsonText = mSettings.getString(SETTINGS_LIST_EAT_PRODUCTS, "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<ProductEat>>() {}.type
            listEat.clear()
            listEat.addAll(Gson().fromJson(gsonText, type))
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
                        "bucket ${bucket.activity} ${getData()} ${SimpleDateFormat("HH:mm").format(
                            bucket.getStartTime(TimeUnit.MILLISECONDS)
                        )} - ${SimpleDateFormat("HH:mm").format(bucket.getEndTime(TimeUnit.MILLISECONDS))}"
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
                mSettings.edit().putInt(SETTINGS_CAL_BURN, burnCal.roundToInt()).apply()
            }
    }


    @SuppressLint("SimpleDateFormat")
    fun getData(): Int {
        val formatData = SimpleDateFormat("dd")
        return formatData.format(Date()).toInt()
    }


    private fun reLoad() {
        val calPerDay = mSettings.getInt(SETTINGS_CAL_PER_FAY, 0)
        val calBurn = mSettings.getInt(SETTINGS_CAL_BURN, 0)
        val calEat = mSettings.getInt(SETTINGS_CAL_EAT, 0)

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        burn_cal.text = calBurn.toString()

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calEat).toString()
        if (cal_left_num_text.text.toString().toInt() < 0) {
            cal_left_num_text.text = "0"
        }

        if (listEat.isNotEmpty()) {
            list_eat_recycler.adapter = MyAdapterProductsEat(listEat)
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
        if (listEat.isNotEmpty()) {
            val gsonText = Gson().toJson(listEat)
            mSettings.edit().putString(SETTINGS_LIST_EAT_PRODUCTS, gsonText).apply()
        }
    }


    class MyAdapterProductsEat(
        private val list: ArrayList<ProductEat>
    ) : RecyclerView.Adapter<MyAdapterProductsEat.MyHolderProductsEat>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderProductsEat {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_product_eat_view, parent, false)
            return MyHolderProductsEat(itemView)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holderProductsEat: MyHolderProductsEat, position: Int) {
            holderProductsEat.bindItem(list[position])
        }

        class MyHolderProductsEat(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindItem(listProduct: ProductEat) {
                itemView.image_product_eat.setImageURI(listProduct.imageUri.toUri())
                itemView.name_eat_view.text = listProduct.name
                itemView.cal_eat_view.text = listProduct.calorieEat.toString()
                itemView.gram_eat_view.text = listProduct.gramsEat.toString()
                itemView.setOnClickListener {
                }
            }
        }
    }
}
