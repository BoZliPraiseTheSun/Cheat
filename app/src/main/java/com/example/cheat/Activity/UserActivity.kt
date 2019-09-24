package com.example.cheat.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.cheat.*
import com.example.cheat.adapter.MyAdapterFoodsEaten
import com.example.cheat.google.AccountGoogle
import com.example.cheat.google.HistoryGoogleFit
import kotlinx.android.synthetic.main.activity_user.*
import kotlin.math.roundToInt

class UserActivity : MvpAppCompatActivity(), UserView {
    override fun installCaloriesEatInSecondaryProgressBar(calories: Int) {
        progress_bar.secondaryProgress = calories
    }

    override fun installCalPerDayInProgressBar(calories: Int) {
        progress_bar.max = calories
    }

    override fun showDaysOnDiet(days: Int) {
        view_days_diet.text = days.toString()
    }

    override fun getSettings(): SharedPreferences {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    override fun showBurnCal(burnCal: Int) {
        burn_cal.text = burnCal.toString()
    }

    companion object {
        lateinit var eatenFoods: ListFoodsEaten
    }

    @InjectPresenter
    lateinit var userPresenter: UserPresenter

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapterFoodsEaten

    private val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mSettings = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        eatenFoods = ListFoodsEaten(mSettings)
        layoutManager = LinearLayoutManager(this)

        initializationAdapter()

        AccountGoogle().singInGoogleAccount(this, this)

        if (mSettings.getInt(getString(R.string.cal_per_day_key), -1) == -1) {
            mSettings.edit().putInt(getString(R.string.cal_per_day_key), 1250).apply()
        }

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ProductsStoreActivity::class.java)
            startActivity(intent)
        }


        list_eat_recycler.layoutManager = layoutManager
        list_eat_recycler.adapter = mAdapter
    }


    private fun initializationAdapter() {
        mAdapter = MyAdapterFoodsEaten(eatenFoods.listFoodsEaten) { foodEaten ->
            Log.d(TAG, "$foodEaten")
        }
    }


    private fun checkNextDay() {
        if (mSettings.getInt(getString(R.string.this_day_key), -1) != getData()) {
            val calBurnAll = mSettings.getInt(getString(R.string.cal_burn_all_key), 0)
            val calBurn = mSettings.getInt(getString(R.string.cal_burn_key), 0)
            val daysOnDiet = mSettings.getInt(getString(R.string.days_on_diet), 0)
            val edit = mSettings.edit()
            edit.putInt(getString(R.string.this_day_key), getData())
            edit.putInt(getString(R.string.cal_burn_all_key), calBurnAll + calBurn)
            edit.putInt(getString(R.string.cal_eat_key), 0)
            edit.putInt(getString(R.string.cal_burn_key), 0)
            edit.putInt(getString(R.string.days_on_diet), daysOnDiet + 1)
            edit.apply()
            eatenFoods.listFoodsEaten.clear()
        }
    }

    private fun reLoad() {
        val calPerDay = mSettings.getInt(getString(R.string.cal_per_day_key), 0)
        val calBurn = mSettings.getInt(getString(R.string.cal_burn_key), 0)
        val calBurnAll = mSettings.getInt(getString(R.string.cal_burn_all_key), 0)
        val calEat = mSettings.getInt(getString(R.string.cal_eat_key), 0)

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        burn_cal.text = calBurn.toString()
        userPresenter.showBurnCal()
        coin.text = (calBurnAll + calBurn).toString()

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calEat).toString()
        if (cal_left_num_text.text.toString().toInt() < 0) {
            cal_left_num_text.text = "0"
        }

        view_days_diet.text = mSettings.getInt(getString(R.string.days_on_diet), 0).toString()

        mAdapter.notifyDataSetChanged()
    }

    private fun caloriesToCoins(cal: Int): Int {
        return (cal * 0.25f).roundToInt()
    }

    private fun setBurnCaloriesInSettings() {
        val burnCal = HistoryGoogleFit().getBurnCaloriesPerThisDay(this)
        mSettings
            .edit()
            .putInt(getString(R.string.cal_burn_key), burnCal.roundToInt())
            .apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> {
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkNextDay()
        reLoad()
    }

    override fun onResume() {
        super.onResume()
        setBurnCaloriesInSettings()
    }

    override fun onStop() {
        super.onStop()
        eatenFoods.setEatenFoods()
    }
}

