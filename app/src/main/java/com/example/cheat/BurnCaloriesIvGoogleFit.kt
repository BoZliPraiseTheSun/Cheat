package com.example.cheat

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit

class BurnCaloriesIvGoogleFit {

    fun getBurnCaloriesForOneDay(context: Context): Float {
        var burnCal = 0f
        Fitness.getHistoryClient(context, GoogleSignIn.getLastSignedInAccount(context)!!)
            .readData(getDataReadRequestBuilder(getTodayZeroTime(), getValidTime()))
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
            .addOnCompleteListener {
                val buckets = it.result!!.buckets
                for (bucket in buckets) {
                    val activityName = bucket.activity.toString()
                    val dataSets = bucket.dataSets
                    if (activityName != "still" &&
                        activityName != "unknown" &&
                        activityName != "in_vehicle") {
                        for (dataSet in dataSets) {
                            val dataPoints = dataSet.dataPoints
                            for (dataPoint in dataPoints) {
                                val avg = dataPoint.getValue(Field.FIELD_CALORIES).asFloat()
                                burnCal += avg
                            }
                        }
                    }
                }
            }
        return burnCal
    }

    fun getTodayZeroTime(): Long {
        val calendar = getValidCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        return calendar.timeInMillis
    }

    fun getValidTime(): Long {
        return getValidCalendar().timeInMillis
    }

    fun getDataReadRequestBuilder(startTime: Long, endTime: Long): DataReadRequest {
        return DataReadRequest.Builder()
            .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
            .bucketByActivityType(1, TimeUnit.MILLISECONDS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
    }

    fun getValidCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar
    }













}