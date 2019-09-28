package com.example.cheat.activity

import com.arellomobile.mvp.MvpView
import com.example.cheat.FoodEaten

interface UserView: MvpView {

    fun showBurnCal(burnCal: Int)

    fun showCaloriesEat(calories: Int)

    fun showCaloriesLeft(calories: Int)

    fun showDaysOnDiet(days: Int)

    fun installCalPerDayInProgressBar(calories: Int)

    fun progressInProgressBar(calories: Int)

    fun installCaloriesEatInSecondaryProgressBar(calories: Int)

    fun getProductEat(list: ArrayList<FoodEaten>)

}