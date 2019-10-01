package com.example.cheat.view

import com.arellomobile.mvp.MvpView
import com.example.cheat.model.FoodEaten

interface UserView: MvpView {

    fun showBurnCal(burnCal: Int)

    fun showDaysOnDiet(days: Int)

    fun getProductEat(list: ArrayList<FoodEaten>)

    fun clearArrayList()

}