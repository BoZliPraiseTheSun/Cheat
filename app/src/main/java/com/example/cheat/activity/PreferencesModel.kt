package com.example.cheat.activity

import android.content.SharedPreferences
import android.util.Log
import com.example.cheat.ListFoodsEaten
import com.example.cheat.ListProducts
import com.example.cheat.Product

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

    fun getFoodEat(): String? {
        return settings.getString(listFoodEatKey, "")
    }

    fun setFoodEat(foodEat: String) {
        edit.putString(listFoodEatKey, foodEat)
    }
}