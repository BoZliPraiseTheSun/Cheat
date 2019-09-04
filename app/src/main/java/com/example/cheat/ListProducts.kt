package com.example.cheat

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListProducts(private val settings: SharedPreferences) {
    private val listProducts: ArrayList<Product> = arrayListOf()
    private val keyPreferences = "list_product_key"

    fun add(element: Product) {
        listProducts.add(element)
    }

    fun getListProducts(): ArrayList<Product> {
        return listProducts
    }

    fun setProducts() {
        if (listProducts.isNotEmpty()) {
            val gsonText = Gson().toJson(listProducts)
            settings.edit().putString(keyPreferences, gsonText).apply()
        }
    }

    private fun getFoodsEaten() {
        val gsonText = settings.getString(keyPreferences, "")
        val type = object : TypeToken<ArrayList<FoodEaten>>() {}.type
        listProducts.addAll(Gson().fromJson(gsonText, type))
    }

    init {
        getFoodsEaten()
    }
}