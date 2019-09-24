package com.example.cheat

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListFoodsEaten(private val settings: SharedPreferences) {
    val listFoodsEaten: ArrayList<FoodEaten> = arrayListOf()
    private val keyPreferencesEat = "list_product_eat_key"


    fun setEatenFoods() {
        val gsonText = Gson().toJson(listFoodsEaten)
        settings.edit().putString(keyPreferencesEat, gsonText).apply()

    }

    fun getFoodsEaten() {
        val gsonText = settings.getString(keyPreferencesEat, "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodEaten>>() {}.type
            listFoodsEaten.addAll(Gson().fromJson(gsonText, type))
        }
    }

    init {
        getFoodsEaten()
    }
}