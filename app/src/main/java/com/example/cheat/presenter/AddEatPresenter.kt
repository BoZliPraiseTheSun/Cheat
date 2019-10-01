package com.example.cheat.presenter

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.cheat.model.FoodEaten
import com.example.cheat.model.PreferencesModel
import com.example.cheat.view.AddEatView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToInt

@InjectViewState
class AddEatPresenter(settings: SharedPreferences) : MvpPresenter<AddEatView>() {

    private val preferences = PreferencesModel(settings)


    fun listenerSeekBar(hundred: Int, ten: Int, unit: Int, calorieContent: Int) {
        val grams = (hundred * 100) + (ten * 10) + unit
        viewState.showGrams(grams)
        val calories = ((calorieContent * grams) / 100f)
            .roundToInt()
        viewState.showCalories(calories)
    }

    fun addFoodEat(newFood: FoodEaten) {
        preferences.setCaloriesEat(preferences.getCaloriesEat() + newFood.caloriesEat)
        val validFoodEat = getProductEat()
        for (food in validFoodEat) {
            if (food.name == newFood.name) {
                food.caloriesEat += newFood.caloriesEat
                food.gramsEat += newFood.gramsEat
                setProductEat(validFoodEat)
                viewState.closeActivity()
                return
            }
        }

        validFoodEat.add(newFood)
        setProductEat(validFoodEat)
        viewState.closeActivity()
    }

    private fun getProductEat(): ArrayList<FoodEaten> {
        val gsonText = preferences.getFoodEat()
        val list = arrayListOf<FoodEaten>()
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodEaten>>() {}.type
            list.addAll(Gson().fromJson(gsonText, type))
        }
        return list
    }

    private fun setProductEat(list: ArrayList<FoodEaten>) {
        val gsonText = Gson().toJson(list)
        preferences.setFoodEat(gsonText)
    }

}