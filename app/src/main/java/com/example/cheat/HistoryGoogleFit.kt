package com.example.cheat

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.*
import java.util.concurrent.TimeUnit

class HistoryGoogleFit {

    fun getBurnCaloriesPerOneDay(context: Context): Float {
        var burnCaloriesPerOneDay = 0f
        Fitness.getHistoryClient(context, GoogleSignIn.getLastSignedInAccount(context)!!)
            .readData(getDataReadRequestBuilder(getTodayZeroTime(), getValidTime()))
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
            .addOnCompleteListener {
                val buckets = removeNoActivity(it.result!!.buckets)
                burnCaloriesPerOneDay = getBurnCalories(buckets)

            }
        return burnCaloriesPerOneDay
    }

    private fun getDataReadRequestBuilder(startTime: Long, endTime: Long): DataReadRequest {
        return DataReadRequest.Builder()
            .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
            .bucketByActivityType(1, TimeUnit.MILLISECONDS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
    }

    private fun removeNoActivity(buckets: MutableList<Bucket>): MutableList<Bucket> {
        val noValidBuckets: MutableList<Bucket> = arrayListOf()
        for (bucket in buckets) {
            val activityName = bucket.activity.toString()
            if (activityName != "still" &&
                activityName != "unknown" &&
                activityName != "in_vehicle"
            ) {
                noValidBuckets.add(bucket)
            }
        }
        buckets.removeAll(noValidBuckets)
        return buckets
    }

    private fun getBurnCalories(buckets: MutableList<Bucket>): Float {
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


}