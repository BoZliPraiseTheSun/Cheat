package com.example.cheat

import com.example.cheat.activity.HintsFood
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("parser")
    fun getProducts(
        @Query("ingr") text: String,
        @Query("app_id") apiId: String = "9ae60612",
        @Query("app_key") appKey: String = "2eb5588fc71b0e21a44892ee50eaa18f"
    ): Call<HintsFood>
}
