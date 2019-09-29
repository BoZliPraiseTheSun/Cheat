package com.example.cheat.activity

import com.example.cheat.Foods
import com.google.gson.annotations.SerializedName

data class HintsFood (
    @SerializedName("hints") var listFoods: ArrayList<Foods>
    )