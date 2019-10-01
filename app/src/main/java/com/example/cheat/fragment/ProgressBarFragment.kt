package com.example.cheat.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.cheat.R
import com.example.cheat.presenter.ProgressBarPresenter
import com.example.cheat.presenter.UserPresenter
import com.example.cheat.view.ProgressBarView
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.progress_bar_fragment.*

class ProgressBarFragment : MvpAppCompatFragment(), ProgressBarView {

    @InjectPresenter
    lateinit var progressBarPresenter: ProgressBarPresenter

    @ProvidePresenter
    fun progressBarPresenter(): ProgressBarPresenter {
        return ProgressBarPresenter(
            activity!!.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.progress_bar_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        progressBarPresenter.installCalPerDayInProgressBar()
        progressBarPresenter.installCaloriesEatInSecondaryProgressBar()
        progressBarPresenter.progressInProgressBar()
        progressBarPresenter.showCaloriesEat()
        progressBarPresenter.showCaloriesLeft()
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

    override fun showCaloriesEat(calories: Int) {
        cal_eat_num_text.text = calories.toString()
    }

    override fun showCaloriesLeft(calories: Int) {
        cal_left_num_text.text = calories.toString()
    }
}
