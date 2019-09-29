package com.example.cheat.model

import android.content.SharedPreferences

class PreferencesModel(private val settings: SharedPreferences) {

    private val calPerDayKey = "calPerDay"
    private val calEatKey = "calEat"
    private val calBurnAllKey = "calBurnAll"
    private val calBurnKey = "calBurn"
    private val thisDayKey = "thisDay"
    private val daysOnDietKey = "daysOnDiet"
    private val listFoodEatKey = "listProduct"

    private val edit = settings.edit()

    fun getCaloriesPerDay(): Int {
        return settings.getInt(calPerDayKey, 0)
    }

    fun setCaloriesPerDay(calPerDay: Int) {
        saveInt(calPerDayKey, calPerDay)
    }


    fun getCaloriesEat(): Int {
        return settings.getInt(calEatKey, 0)
    }

    fun setCaloriesEat(calEat: Int) {
        saveInt(calEatKey, calEat)
    }


    fun getCaloriesBurnAll(): Int {
        return settings.getInt(calBurnAllKey, 0)
    }

    fun setCaloriesBurnAll(calBurn: Int) {
        saveInt(calBurnAllKey, calBurn)
    }


    fun getCaloriesBurn(): Int {
        return settings.getInt(calBurnKey, 0)
    }

    fun setCaloriesBurn(calBurn: Int) {
        saveInt(calBurnKey, calBurn)
    }


    fun getThisDay(): Int {
        return settings.getInt(thisDayKey, 0)
    }

    fun setThisDay(day: Int) {
        saveInt(thisDayKey, day)
    }


    fun getDaysOnDiet(): Int {
        return settings.getInt(daysOnDietKey, 0)
    }

    fun setDaysOnDiet(days: Int) {
        saveInt(daysOnDietKey, days)
    }

    fun getFoodEat(): String? {
        return settings.getString(listFoodEatKey, "")
    }

    fun setFoodEat(foodEat: String) {
        saveString(listFoodEatKey, foodEat)
    }

    private fun saveInt(key: String, value: Int) {
        edit.putInt(key, value).apply()
    }

    private fun saveString(key: String, value: String) {
        edit.putString(key, value).apply()
    }
}