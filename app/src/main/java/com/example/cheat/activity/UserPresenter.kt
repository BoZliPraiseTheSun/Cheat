package com.example.cheat.activity

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import java.text.SimpleDateFormat
import java.util.*

@InjectViewState
class UserPresenter: MvpPresenter<UserView>() {

    private val dataFormatDD = SimpleDateFormat("dd", Locale.UK)
    private val preferences = PreferencesModel(viewState.getSettings())

    fun showBurnCal() {
        viewState.showBurnCal(preferences.getCaloriesBurnAll() + preferences.getCaloriesBurn())
    }

    fun installCalPerDayInProgressBar() {
        viewState.installCalPerDayInProgressBar(preferences.getCaloriesPerDay() * 2)
    }

    fun installCaloriesEatInSecondaryProgressBar() {
        viewState.installCaloriesEatInSecondaryProgressBar(preferences.getCaloriesEat())
    }

    fun showDaysOnDiet() {
    }


    private fun getData(): Int {
        return dataFormatDD.format(Date()).toInt()
    }
}