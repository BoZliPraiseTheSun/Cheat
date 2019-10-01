package com.example.cheat.view

import com.arellomobile.mvp.MvpView

interface ProgressBarView: MvpView {

    fun showCaloriesEat(calories: Int)

    fun showCaloriesLeft(calories: Int)

    fun installCalPerDayInProgressBar(calories: Int)

    fun progressInProgressBar(calories: Int)

    fun installCaloriesEatInSecondaryProgressBar(calories: Int)
}