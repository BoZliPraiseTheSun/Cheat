package com.example.cheat.view

import com.arellomobile.mvp.MvpView
import com.example.cheat.retrofitModel.Foods

interface ProductStoreView: MvpView {

    fun refreshListFoods(list: ArrayList<Foods>)

}