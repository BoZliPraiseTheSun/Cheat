package com.example.cheat.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.cheat.model.FoodEaten
import com.example.cheat.R
import kotlinx.android.synthetic.main.activity_product_add_eat.*
import kotlin.math.roundToInt

class ProductAddEatActivity : AppCompatActivity() {

    private lateinit var mSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_add_eat)

        mSettings =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)


        product_name.text = intent.getStringExtra("name")
        val calorieContent = intent.getIntExtra("calories", 0)

        gram_100.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setGramText()
                gram_to_cal_text.text = viewCal(calorieContent, p1 * 100, gram_10.progress * 10, gram_1.progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        gram_10.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setGramText()
                gram_to_cal_text.text = viewCal(calorieContent, gram_100.progress * 100, p1 * 10, gram_1.progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        gram_1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setGramText()
                gram_to_cal_text.text = viewCal(calorieContent, gram_100.progress * 100, gram_10.progress * 10, p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        add_product_btn.setOnClickListener {
            val calEat = mSettings.getInt(getString(R.string.cal_eat_key), 0)
            mSettings
                .edit()
                .putInt(
                    getString(R.string.cal_eat_key),
                    gram_to_cal_text.text.toString().toInt() + calEat
                )
                .apply()

            var createProduct = true
            if (UserActivity.eatenFoods.isNotEmpty()) {
                for (i in UserActivity.eatenFoods) {
                    if (i.name == product_name.text.toString()) {
                        i.calorieEat += gram_to_cal_text.text.toString().toInt()
                        i.gramsEat += gram_100.progress * 100 + gram_10.progress * 10 + gram_1.progress
                        createProduct = false
                    }
                }
            }
            if (createProduct) {
                UserActivity.eatenFoods.add(
                    FoodEaten(
                        product_name.text.toString(),
                        gram_to_cal_text.text.toString().toInt(),
                        gram_100.progress * 100 + gram_10.progress * 10 + gram_1.progress
                    )
                )
            }
            finish()
        }


    }

    fun setGramText() {
        view_gram.text = (gram_100.progress * 100 + gram_10.progress * 10 + gram_1.progress).toString()
    }

    private fun viewCal(calorieContent: Int, gram100: Int, gram10: Int, gram1: Int): String {
        return ((calorieContent * (gram100 + gram10 + gram1)) / 100f)
            .roundToInt()
            .toString()
    }

}
