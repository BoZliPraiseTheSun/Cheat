package com.example.cheat.activity

import android.content.SharedPreferences
import com.arellomobile.mvp.MvpView

interface UserView: MvpView {

    fun showBurnCal(burnCal: Int)

    fun installCaloriesEatInSecondaryProgressBar(calories: Int)

    fun installCalPerDayInProgressBar(calories: Int)

    fun showDaysOnDiet(days: Int)

    fun getSettings(): SharedPreferences
}