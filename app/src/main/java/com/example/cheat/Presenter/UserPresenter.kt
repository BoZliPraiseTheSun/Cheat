package com.example.cheat.Presenter

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.cheat.FoodEaten
import com.example.cheat.view.UserView
import com.example.cheat.model.PreferencesModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@InjectViewState
class UserPresenter(settings: SharedPreferences) : MvpPresenter<UserView>() {

    private val dataFormatDD = SimpleDateFormat("dd", Locale.UK)
    private val preferences = PreferencesModel(settings)

    fun showBurnCal() {
        viewState.showBurnCal(preferences.getCaloriesBurnAll() + preferences.getCaloriesBurn())
    }

    fun installCalPerDayInProgressBar() {
        viewState.installCalPerDayInProgressBar(preferences.getCaloriesPerDay() * 2)
    }

    fun installCaloriesEatInSecondaryProgressBar() {
        viewState.installCaloriesEatInSecondaryProgressBar(preferences.getCaloriesEat())
    }

    fun showCaloriesEat() {
        viewState.showCaloriesEat(preferences.getCaloriesEat())
    }

    fun showCaloriesLeft() {
        var caloriesLeft = preferences.getCaloriesPerDay() - preferences.getCaloriesEat()
        if (caloriesLeft < 0) {
            caloriesLeft = 0
        }
        viewState.showCaloriesLeft(caloriesLeft)
    }

    fun progressInProgressBar() {
        viewState.progressInProgressBar(preferences.getCaloriesEat())
    }

    fun showDaysOnDiet() {
    }

    fun checkNextDay() {
        if (getData() != preferences.getThisDay()) {
            preferences.setCaloriesBurnAll(preferences.getCaloriesBurnAll() + preferences.getCaloriesBurn())
            preferences.setCaloriesBurn(0)
            preferences.setDaysOnDiet(preferences.getDaysOnDiet() + 1)
            preferences.setCaloriesEat(0)
            preferences.setThisDay(getData())
        }
    }

    fun getProductEat() {
        val gsonText = preferences.getFoodEat()
        val list = arrayListOf<FoodEaten>()
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<FoodEaten>>() {}.type
            list.addAll(Gson().fromJson(gsonText, type))
            viewState.getProductEat(list)
        }
    }

    fun setProductEat(list: ArrayList<FoodEaten>) {
        val gsonText = Gson().toJson(list)
        preferences.setFoodEat(gsonText)
    }

    private fun getData(): Int {
        return dataFormatDD.format(Date()).toInt()
    }
}