package com.example.cheat.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat.Adapter.MyAdapterProduct
import com.example.cheat.FoodEaten
import com.example.cheat.Product
import com.example.cheat.R
import com.example.cheat.slowScroll
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_products_store.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ProductsStoreActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_TAKE_PHOTO = 334
        const val REQUEST_CREATE_PRODUCT = 333
    }

    private val TAG = "ProductsStoreActivity"

    var calIn100Gram = -1

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mImageUri: Uri
    private lateinit var mAdapter: MyAdapterProduct

    private val products: ArrayList<Product> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_store)

        initializationLateInitParam()


        product_list_recycler.layoutManager = layoutManager

        put_cal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                view_cal_in_seek_bar.text = p1.toString()
                gram_to_cal_text.text = viewCal(calIn100Gram, p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        deleted_product.setOnClickListener {
            products.remove(Product(product_name.text.toString(), calIn100Gram))
            add_product.visibility = View.GONE
            mAdapter.notifyDataSetChanged()

        }

        add_product_btn.setOnClickListener {
            add_product.visibility = View.GONE
            val calEat = mSettings.getInt(getString(R.string.cal_eat_key), 0)
            mSettings
                .edit()
                .putInt(
                    getString(R.string.cal_eat_key),
                    gram_to_cal_text.text.toString().toInt() + calEat
                )
                .apply()

            val listEat = UserActivity.eatenFoods
            var createProduct = true
            if (listEat.isNotEmpty()) {
                Log.d(TAG, "eatenFoods.add")
                for (i in listEat) {
                    if (i.name == product_name.text.toString()) {
                        i.calorieEat += gram_to_cal_text.text.toString().toInt()
                        i.gramsEat += put_cal.progress.toString().toInt()
                        createProduct = false
                    }
                }
            }
            if (createProduct) {
                Log.d(TAG, "eatenFoods.create")
                listEat.add(
                    FoodEaten(
                        product_name.text.toString(),
                        gram_to_cal_text.text.toString().toInt(),
                        put_cal.progress.toString().toInt()
                    )
                )
            }

            put_cal.progress = 0
            slowScroll(scroll_view, TAG)
        }

        test_add_product.setOnClickListener {
            val intent = Intent(this, AddNewProductActivity::class.java)
            startActivity(intent)
        }

        getListProduct()
        if (products.isEmpty()) {
            firstOpenApp()
        }
        createRecyclerView(products)
    }

    private fun initializationLateInitParam() {
        mSettings =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)
    }


    private fun getListProduct() {
        val gsonText = mSettings.getString(getString(R.string.list_product_key), "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            products.clear()
            products.addAll(Gson().fromJson(gsonText, type))
        }
    }

    private fun viewCal(calorieContent: Int, gram: Int): String {
        return ((calorieContent * put_cal.progress.toString().toInt()) / 100f)
            .roundToInt()
            .toString()
    }


    private fun createRecyclerView(list: ArrayList<Product>) {
        mAdapter = MyAdapterProduct(
            list
        ) { product ->
            calIn100Gram = product.calorieContent
            product_name.text = product.name
            gram_to_cal_text.text = viewCal(product.calorieContent, put_cal.progress)
            if (add_product.visibility == View.GONE) {
                add_product.visibility = View.VISIBLE
            }
            slowScroll(scroll_view, TAG)
        }
        product_list_recycler.adapter = mAdapter

    }

    private fun firstOpenApp() {
        products.add(Product("Булгур", 342))
        products.add(Product("Яблоко", 47))
        products.add(Product("Сливочный сыр", 210))
        products.add(Product("Грудка куриная (мангал)", 175))
        products.add(Product("Нектарин", 63))
        products.add(Product("Борщ", 45))
        products.add(Product("Хлебцы Dr.Korner травы", 300))
        products.add(Product("Хлебцы Dr.Korner карамель", 320))
        products.add(Product("Огурец", 12))
        products.add(Product("Помидор", 20))
        products.add(Product("Яйцо (белок)", 48))
        products.add(Product("Груша", 47))
        products.add(Product("Мандарин", 38))
        products.add(Product("Хлеб белый", 240))
        products.add(Product("Виноград", 72))
        products.add(Product("Тунец (консервы)", 96))
        products.add(Product("Яйцо", 157))
        products.add(Product("Банан", 96))
        products.add(Product("Кабачок", 24))
        products.add(Product("Картошка", 77))
        products.add(Product("Грудка куриная", 164))
        products.add(Product("Морковь", 35))
        products.add(Product("Яйцо (желток)", 354))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CREATE_PRODUCT -> {
                    if (data != null) {
                        products.add(
                            Product(
                                data.getStringExtra("Name")!!,
                                data.getIntExtra("Calorie Content", -1)
                            )
                        )
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val gsonText = Gson().toJson(products)
        mSettings.edit().putString(getString(R.string.list_product_key), gsonText).apply()
    }

}