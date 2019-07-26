package com.example.cheat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ParcelableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.list_product_eat_view.view.*
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
        val listEat: ArrayList<ProductEat> = arrayListOf()
    }

    lateinit var mSettings: SharedPreferences
    lateinit var layoutManager: RecyclerView.LayoutManager
    val TAG = "UserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mSettings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)

        if (mSettings.getInt(SETTINGS_CAL_PER_FAY, -1) == -1) {
            mSettings.edit().putInt(SETTINGS_CAL_PER_FAY, 1250).apply()
        }

        list_eat_recycler.layoutManager = LinearLayoutManager(this)

        nextDay()
        getListEat()

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
            edit.putString(SETTINGS_LIST_EAT_PRODUCTS, "")
            edit.apply()
            listEat.clear()
        }
    }

    fun getListEat(){
        val gsonTextt = mSettings.getString(SETTINGS_LIST_EAT_PRODUCTS, "")
        if (gsonTextt != "") {
            val type = object : TypeToken<ArrayList<ProductEat>>() {}.type
            listEat.clear()
            listEat.addAll(Gson().fromJson(gsonTextt, type))
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getData(): Int {
        val formatData = SimpleDateFormat("DD")
        return formatData.format(Date()).toInt()
    }

    override fun onResume() {
        super.onResume()
        val calPerDay = mSettings.getInt(SETTINGS_CAL_PER_FAY, 0)
        val calBurn = mSettings.getInt(SETTINGS_CAL_BURN, 0)
        val calEat = mSettings.getInt(SETTINGS_CAL_EAT, 0)
        Log.d(TAG, "Settings $calEat")

        progress_bar.max = (calPerDay + calBurn) * 2
        progress_bar.secondaryProgress = calEat
        progress_bar.progress = calEat

        cal_ean_num_text.text = calEat.toString()
        cal_left_num_text.text = ((calPerDay + calBurn) - calEat).toString()
        if (cal_left_num_text.text.toString().toInt() < 0) {
            cal_left_num_text.text = "0"
        }

        if (listEat.isNotEmpty()) {
            list_eat_recycler.adapter = MyAdapterProductsEat(listEat)
        }
    }

    override fun onPause() {
        super.onPause()
        if (listEat.isNotEmpty()) {
            val gsonText = Gson().toJson(listEat)
            mSettings.edit().putString(SETTINGS_LIST_EAT_PRODUCTS, gsonText).apply()
        }
    }

    class MyAdapterProductsEat(
        private val list: ArrayList<ProductEat>
    ) : RecyclerView.Adapter<MyAdapterProductsEat.MyHolderProductsEat>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderProductsEat {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_product_eat_view, parent, false)
            return MyHolderProductsEat(itemView)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holderProductsEat: MyHolderProductsEat, position: Int) {
            holderProductsEat.bindItem(list[position])
        }

        class MyHolderProductsEat(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindItem(listProduct: ProductEat) {
                itemView.image_product_eat.setImageResource(listProduct.image)
                itemView.name_eat_view.text = listProduct.name
                itemView.cal_eat_view.text = listProduct.calorieEat.toString()
                itemView.gram_eat_view.text = listProduct.gramsEat.toString()
                itemView.setOnClickListener {
                }
            }
        }
    }
}
