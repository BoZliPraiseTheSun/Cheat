package com.example.cheat.model

import com.google.gson.annotations.SerializedName

data class HintsFood (
    @SerializedName("hints") var listFoods: ArrayList<Foods>
    )