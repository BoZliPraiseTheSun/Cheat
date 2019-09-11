package com.example.cheat

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListFoodsEaten(private val settings: SharedPreferences) {
    val listFoodsEaten: ArrayList<FoodEaten> = arrayListOf()
    private val keyPreferences = "list_product_eat_key"



    fun setEatenFoods() {
        if (listFoodsEaten.isNotEmpty()) {
            val gsonText = Gson().toJson(listFoodsEaten)
            settings.edit().putString(keyPreferences, gsonText).apply()
        }
    }

    fun getFoodsEaten() {
        val gsonText = settings.getString(keyPreferences, "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodEaten>>() {}.type
            listFoodsEaten.addAll(Gson().fromJson(gsonText, type))
        }
    }

    init {
        getFoodsEaten()
    }
}