package com.example.cheat.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.cheat.presenter.AddEatPresenter
import com.example.cheat.model.FoodEaten
import com.example.cheat.R
import com.example.cheat.view.AddEatView
import kotlinx.android.synthetic.main.activity_add_eat.*

class AddEatActivity : MvpAppCompatActivity(), AddEatView {


    @InjectPresenter
    lateinit var addEatPresenter: AddEatPresenter

    @ProvidePresenter
    fun addEatPresenter(): AddEatPresenter {
        return AddEatPresenter(
            getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
        )
    }

    private lateinit var foodName: String
    private var calorieContent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_eat)

        foodName = intent.getStringExtra("name")!!
        calorieContent = intent.getIntExtra("calories", 0)

        product_name.text = foodName


        gram_100.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                putInfoInListener()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        gram_10.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                putInfoInListener()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        gram_1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                putInfoInListener()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        add_product_btn.setOnClickListener {
            addEatPresenter.addFoodEat(
                FoodEaten(
                    foodName,
                    calories_view.text.toString().toInt(),
                    gram_view.text.toString().toInt()
                )
            )
        }
    }

    fun putInfoInListener() {
        addEatPresenter.listenerSeekBar(
            gram_100.progress,
            gram_10.progress,
            gram_1.progress,
            calorieContent
        )
    }

    override fun showGrams(gram: Int) {
        gram_view.text = gram.toString()
    }

    override fun showCalories(calories: Int) {
        calories_view.text = calories.toString()
    }

    override fun closeActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
