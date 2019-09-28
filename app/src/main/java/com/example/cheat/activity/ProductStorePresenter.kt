package com.example.cheat.activity

import android.util.Log
import com.arellomobile.mvp.MvpPresenter
import com.example.cheat.Api
import com.example.cheat.FoodDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductStorePresenter: MvpPresenter<ProductStoreView>() {

    val baseURL = "https://api.edamam.com/api/food-database/"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)


    fun getProducts(text: String){
        retrofit
            .getProducts(text = text)
            .enqueue(object : Callback<FoodDatabase>{
                override fun onFailure(call: Call<FoodDatabase>, t: Throwable) {
                    Log.d("Retrofit", "Throwable")
                    Log.d("Retrofit", "$t")
                }

                override fun onResponse(
                    call: Call<FoodDatabase>,
                    response: Response<FoodDatabase>
                ) {
                    Log.d("retrofit", "not null")
                    Log.d("retrofit", "${response.body()!!.hints}")
                }

            })
    }
}