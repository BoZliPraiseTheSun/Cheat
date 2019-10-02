package com.example.cheat.presenter

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.cheat.log
import com.example.cheat.model.FoodEaten
import com.example.cheat.view.UserView
import com.example.cheat.model.PreferencesModel
import com.google.android.gms.fitness.data.Bucket
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@InjectViewState
class UserPresenter(settings: SharedPreferences) : MvpPresenter<UserView>() {

    private val dataFormatDD = SimpleDateFormat("dd", Locale.UK)
    private val preferences = PreferencesModel(settings)

    fun showBurnCal() {
        viewState.showBurnCal(preferences.getCaloriesBurn())
    }

    fun setBurnCal(calories: Float) {
        preferences.setCaloriesBurn(calories.roundToInt())
    }


    fun showDaysOnDiet() {
        viewState.showDaysOnDiet(preferences.getDaysOnDiet())
    }

    fun checkNextDay() {
        if (getData() != preferences.getThisDay()) {
            preferences.setCaloriesBurnAll(preferences.getCaloriesBurnAll() + preferences.getCaloriesBurn())
            preferences.setCaloriesBurn(0)
            preferences.setDaysOnDiet(preferences.getDaysOnDiet() + 1)
            preferences.setCaloriesEat(0)
            preferences.setThisDay(getData())
            preferences.setFoodEat("")
            viewState.clearArrayList()
        }
    }

    fun getProductEat() {
        val gsonText = preferences.getFoodEat()
        val list = arrayListOf<FoodEaten>()
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodEaten>>() {}.type
            list.addAll(Gson().fromJson(gsonText, type))
            viewState.clearArrayList()
            viewState.getProductEat(list)
        }
    }


    fun getDataReadRequestBuilder(): DataReadRequest {
        return DataReadRequest.Builder()
            .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
            .bucketByActivityType(1, TimeUnit.MILLISECONDS)
            .setTimeRange(getTodayZeroTime(), getValidTime(), TimeUnit.MILLISECONDS)

            .build()
    }

    fun handleBucket(buckets: MutableList<Bucket>): Float {
        val bucket = removeNoActivity(buckets)
        val calories = countBurnCalories(bucket)
        return calories
    }

    private fun removeNoActivity(buckets: MutableList<Bucket>): MutableList<Bucket> {
        val noValidBuckets: MutableList<Bucket> = arrayListOf()
        for (bucket in buckets) {
            val activityName = bucket.activity.toString()
            if (activityName == "still" ||
                activityName == "unknown" ||
                activityName == "in_vehicle"
            ) {
                noValidBuckets.add(bucket)
            }
        }
        buckets.removeAll(noValidBuckets)
        return buckets
    }

    private fun countBurnCalories(buckets: MutableList<Bucket>): Float {
        var burnCal = 0f
        for (bucket in buckets) {
            val dataSets = bucket.dataSets
            for (dataSet in dataSets) {
                val dataPoints = dataSet.dataPoints
                for (dataPoint in dataPoints) {
                    val avg = dataPoint.getValue(Field.FIELD_CALORIES).asFloat()
                    burnCal += avg
                }
            }
        }
        return burnCal
    }

    private fun getTodayZeroTime(): Long {
        val calendar = getValidCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        return calendar.timeInMillis
    }

    private fun getValidTime(): Long {
        return getValidCalendar().timeInMillis
    }

    private fun getValidCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    private fun getData(): Int {
        return dataFormatDD.format(Date()).toInt()
    }
}
