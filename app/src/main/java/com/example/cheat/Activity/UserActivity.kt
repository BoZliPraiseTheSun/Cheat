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
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.cheat.*
import com.example.cheat.Presenter.UserPresenter
import com.example.cheat.adapter.MyAdapterFoodsEaten
import com.example.cheat.google.AccountGoogle
import com.example.cheat.model.FoodEaten
import com.example.cheat.view.UserView
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : MvpAppCompatActivity(), UserView {

    companion object {
        val eatenFoods = arrayListOf<FoodEaten>()
    }


    @InjectPresenter
    lateinit var userPresenter: UserPresenter

    @ProvidePresenter
    fun provideUserPresenter(): UserPresenter {
        return UserPresenter(
            getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
        )
    }


    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapterFoodsEaten

    private val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mSettings = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        userPresenter.getProductEat()
        layoutManager = LinearLayoutManager(this)


        AccountGoogle().singInGoogleAccount(this, this)

        if (mSettings.getInt(getString(R.string.cal_per_day_key), -1) == -1) {
            mSettings.edit().putInt(getString(R.string.cal_per_day_key), 1250).apply()
        }

        go_bay_btn.setOnClickListener {
            val intent = Intent(this, ProductsStoreActivity::class.java)
            startActivity(intent)
        }

        mAdapter = MyAdapterFoodsEaten(eatenFoods) { foodEaten ->
            Log.d(TAG, "$foodEaten")
        }

        list_eat_recycler.layoutManager = layoutManager
        list_eat_recycler.adapter = mAdapter
    }



    private fun reLoad() {
        userPresenter.installCalPerDayInProgressBar()
        userPresenter.installCaloriesEatInSecondaryProgressBar()
        userPresenter.progressInProgressBar()

        userPresenter.showBurnCal()

        userPresenter.showCaloriesEat()
        userPresenter.showCaloriesLeft()

        userPresenter.showDaysOnDiet()

        mAdapter.notifyDataSetChanged()
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
        userPresenter.checkNextDay()
    }

    override fun onResume() {
        super.onResume()
        reLoad()
    }

    override fun onStop() {
        super.onStop()
        userPresenter.setProductEat(eatenFoods)
    }



    override fun installCaloriesEatInSecondaryProgressBar(calories: Int) {
        progress_bar.secondaryProgress = calories
    }

    override fun installCalPerDayInProgressBar(calories: Int) {
        progress_bar.max = calories
    }

    override fun progressInProgressBar(calories: Int) {
        progress_bar.progress = calories
    }

    override fun showDaysOnDiet(days: Int) {
        view_days_diet.text = days.toString()
    }

    override fun showCaloriesEat(calories: Int) {
        cal_eat_num_text.text = calories.toString()
    }

    override fun showCaloriesLeft(calories: Int) {
        cal_left_num_text.text = calories.toString()
    }

    override fun showBurnCal(burnCal: Int) {
        burn_cal.text = burnCal.toString()
    }

    override fun getProductEat(list: ArrayList<FoodEaten>) {
        eatenFoods.addAll(list)
    }
}

