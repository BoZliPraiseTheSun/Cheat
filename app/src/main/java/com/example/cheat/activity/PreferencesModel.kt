package com.example.cheat.activity

import android.content.SharedPreferences
import com.example.cheat.ListFoodsEaten
import com.example.cheat.ListProducts
import com.example.cheat.Product

class PreferencesModel(private val settings: SharedPreferences) {

    private val calPerDayKey = "cal_per_day_key"
    private val calEatKey = "cal_per_day_key"
    private val calBurnAllKey = "cal_per_day_key"
    private val calBurnKey = "cal_per_day_key"
    private val thisDayKey = "cal_per_day_key"
    private val daysOnDietKey = "cal_per_day_key"

    private val edit = settings.edit()

    fun getCaloriesPerDay(): Int {
        return settings.getInt(calPerDayKey, 0)
    }

    fun setCaloriesPerDay(calPerDay: Int) {
        edit.putInt(calPerDayKey, calPerDay)
    }


    fun getCaloriesEat(): Int {
        return settings.getInt(calEatKey, 0)
    }

    fun setCaloriesEat(calEat: Int) {
        edit.putInt(calEatKey, calEat)
    }


    fun getCaloriesBurnAll(): Int {
        return settings.getInt(calBurnAllKey, 0)
    }

    fun setCaloriesBurnAll(calBurn: Int) {
        edit.putInt(calBurnAllKey, calBurn)
    }


    fun getCaloriesBurn(): Int {
        return settings.getInt(calBurnKey, 0)
    }

    fun setCaloriesBurn(calBurn: Int) {
        edit.putInt(calBurnKey, calBurn)
    }


    fun getThisDay(): Int {
        return settings.getInt(thisDayKey, 0)
    }

    fun setThisDay(day: Int) {
        edit.putInt(thisDayKey, day)
    }


    fun getDaysOnDiet(): Int {
        return settings.getInt(daysOnDietKey, 0)
    }

    fun setDaysOnDiet(days: Int) {
        edit.putInt(daysOnDietKey, days)
    }
}