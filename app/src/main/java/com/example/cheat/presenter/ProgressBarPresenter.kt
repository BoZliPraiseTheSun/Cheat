package com.example.cheat.presenter

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.cheat.model.PreferencesModel
import com.example.cheat.view.ProgressBarView


@InjectViewState
class ProgressBarPresenter(settings: SharedPreferences): MvpPresenter<ProgressBarView>() {

    private val preferences = PreferencesModel(settings)

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

}