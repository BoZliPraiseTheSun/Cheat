package com.example.cheat.activity

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import java.text.SimpleDateFormat
import java.util.*

@InjectViewState
class UserPresenter(settings: SharedPreferences): MvpPresenter<UserView>() {

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

    private fun getData(): Int {
        return dataFormatDD.format(Date()).toInt()
    }
}