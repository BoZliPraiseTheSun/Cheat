package com.example.cheat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_user.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserActivity : AppCompatActivity() {

    companion object {
        const val SETTINGS = "mSettings"
        const val SETTINGS_CAL_PER_FAY = "calPerDay"
        const val SETTINGS_CAL_EAT = "calEat"
        const val SETTINGS_CAL_BURN = "calBurn"
        const val SETTINGS_LIST_EAT_PRODUCTS = "listEatProduct"
        const val SETTINGS_THIS_DAY = "thisDay"
    }

    lateinit var listEat: ArrayList<Int>
    lateinit var mSettings: SharedPreferences
    lateinit var layoutManager: RecyclerView.LayoutManager
    val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        listEat = arrayListOf()
        mSettings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)

        if (mSettings.getInt(SETTINGS_CAL_PER_FAY, -1) == -1) {
            mSettings.edit().putInt(SETTINGS_CAL_PER_FAY, 1250).apply()
        }

        nextDay()

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ListProductsActivity::class.java)
            startActivity(intent)
        }
    }

    fun nextDay() {
        if (mSettings.getInt(SETTINGS_THIS_DAY, -1) != getData()) {
            val edit = mSettings.edit()
            edit.putInt(SETTINGS_THIS_DAY, getData())
            edit.putInt(SETTINGS_CAL_EAT, 0)
            edit.apply()
            listEat.clear()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getData(): Int {
        val formatData = SimpleDateFormat("HH")
        return formatData.format(Date()).toInt()
    }

    override fun onResume() {
        super.onResume()
        val calPerDay = mSettings.getInt(SETTINGS_CAL_PER_FAY, 0)
        val calBurn = mSettings.getInt(SETTINGS_CAL_BURN, 0)
        val calEat = mSettings.getInt(SETTINGS_CAL_EAT, 0)
        Log.d(TAG, "Settings ${calEat}")

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calEat).toString()
        if (cal_left_num_text.text.toString().toInt() < 0) {
            cal_left_num_text.text = "0"
        }
    }
}
