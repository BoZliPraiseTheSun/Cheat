package com.example.cheat.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.cheat.api.Api
import com.example.cheat.retrofitModel.HintsFood
import com.example.cheat.view.ProductStoreView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InjectViewState
class ProductStorePresenter : MvpPresenter<ProductStoreView>() {

    val baseURL = "https://api.edamam.com/api/food-database/"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)


    fun getProducts(text: String) {
        retrofit
            .getProducts(text = text)
            .enqueue(object : Callback<HintsFood> {
                override fun onFailure(call: Call<HintsFood>, t: Throwable) {
                    Log.d("Retrofit", "Throwable")
                    Log.d("Retrofit", "$t")
                }

                override fun onResponse(
                    call: Call<HintsFood>,
                    response: Response<HintsFood>
                ) {
                    val list = response.body()!!.listFoods
                    viewState.refreshListFoods(list)
                    viewState.addAdapter()
                    viewState.addAdapterInRecycler()
                }

            })
    }

    private fun addListInAdapter() {

    }
}