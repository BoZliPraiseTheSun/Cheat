package com.example.cheat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    companion object {
        const val SETTINGS = "mSettings"
    }

    lateinit var mSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mSettings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ListProductsActivity::class.java)
            startActivity(intent)
        }
    }
}
