package com.example.cheat.view

import com.arellomobile.mvp.MvpView
import com.example.cheat.model.FoodEaten

interface AddEatView: MvpView {

    fun closeActivity()

    fun showGrams(gram: Int)

    fun showCalories(calories: Int)
}