package com.example.cheat.view

import com.arellomobile.mvp.MvpView
import com.example.cheat.retrofitModel.Foods

interface ProductStoreView: MvpView {

    fun addAdapter(list: ArrayList<Foods>)

    fun addAdapterInRecycler()

}