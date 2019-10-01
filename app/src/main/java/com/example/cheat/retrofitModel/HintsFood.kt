package com.example.cheat.retrofitModel

import com.google.gson.annotations.SerializedName

data class HintsFood (
    @SerializedName("hints") var listFoods: ArrayList<Foods>
    )