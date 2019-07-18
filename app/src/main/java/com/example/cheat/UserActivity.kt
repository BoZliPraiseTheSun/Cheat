package com.example.cheat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    companion object {
        const val SETTINGS = "mSettings"
        const val SETTINGS_CAL_PER_FAY = "mSettings"
        const val SETTINGS_CAL_PER_DAY_LEFT = "mSettings"
        const val SETTINGS_CAL_EAT = "mSettings"
        const val SETTINGS_CAL_BURN = "mSettings"
        const val SETTINGS_LIST_EAT_PRODUCTS = "mSettings"
        const val SETTINGS_THIS_DAY = "mSettings"
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

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ListProductsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val calPerDay = mSettings.getInt(SETTINGS_CAL_PER_FAY, 0)
        val calBurn = mSettings.getInt(SETTINGS_CAL_BURN, 0)
        val calEat = mSettings.getInt(SETTINGS_CAL_EAT, 0)

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calBurn).toString()
        if (cal_left_num_text.text.toString().toInt() > 0) {
            cal_left_num_text.text = "0"
        }
    }
}
