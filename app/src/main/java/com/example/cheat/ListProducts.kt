package com.example.cheat

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListProducts(private val settings: SharedPreferences) {
    var listProducts: ArrayList<Product> = arrayListOf()
    private val keyPreferences = "list_product_key"

    fun setProducts() {
        if (listProducts.isNotEmpty()) {
            val gsonText = Gson().toJson(listProducts)
            settings.edit().putString(keyPreferences, gsonText).apply()
        }
    }

    fun getFoodsEaten() {
        val gsonText = settings.getString(keyPreferences, "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            listProducts.addAll(Gson().fromJson(gsonText, type))
        }
    }

    init {
        getFoodsEaten()
    }
}