package com.example.cheat.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat.*
import com.example.cheat.adapter.MyAdapterProduct
import kotlinx.android.synthetic.main.activity_products_store.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ProductsStoreActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CREATE_PRODUCT = 333
    }

    private val TAG = "ProductsStoreActivity"

    var calIn100Gram = -1

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapterProduct
    private lateinit var products: ListProducts


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_store)

        initializationLateInitParam()
        products = ListProducts(mSettings)


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
            products.listProducts.remove(Product(product_name.text.toString(), calIn100Gram))
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

            var createProduct = true
            if (UserActivity.eatenFoods.listFoodsEaten.isNotEmpty()) {
                Log.d(TAG, "eatenFoods.add")
                for (i in UserActivity.eatenFoods.listFoodsEaten) {
                    if (i.name == product_name.text.toString()) {
                        i.calorieEat += gram_to_cal_text.text.toString().toInt()
                        i.gramsEat += put_cal.progress.toString().toInt()
                        createProduct = false
                    }
                }
            }
            if (createProduct) {
                Log.d(TAG, "eatenFoods.create")
                UserActivity.eatenFoods.listFoodsEaten.add(
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


        if (products.listProducts.isEmpty()) {
            firstOpenApp()
        }
        createRecyclerView(products.listProducts)
    }

    private fun initializationLateInitParam() {
        mSettings =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)
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
        products.listProducts.add(Product("Булгур", 342))
        products.listProducts.add(Product("Яблоко", 47))
        products.listProducts.add(Product("Сливочный сыр", 210))
        products.listProducts.add(Product("Грудка куриная (мангал)", 175))
        products.listProducts.add(Product("Нектарин", 63))
        products.listProducts.add(Product("Борщ", 45))
        products.listProducts.add(Product("Хлебцы Dr.Korner травы", 300))
        products.listProducts.add(Product("Хлебцы Dr.Korner карамель", 320))
        products.listProducts.add(Product("Огурец", 12))
        products.listProducts.add(Product("Помидор", 20))
        products.listProducts.add(Product("Яйцо (белок)", 48))
        products.listProducts.add(Product("Груша", 47))
        products.listProducts.add(Product("Мандарин", 38))
        products.listProducts.add(Product("Хлеб белый", 240))
        products.listProducts.add(Product("Виноград", 72))
        products.listProducts.add(Product("Тунец (консервы)", 96))
        products.listProducts.add(Product("Яйцо", 157))
        products.listProducts.add(Product("Банан", 96))
        products.listProducts.add(Product("Кабачок", 24))
        products.listProducts.add(Product("Картошка", 77))
        products.listProducts.add(Product("Грудка куриная", 164))
        products.listProducts.add(Product("Морковь", 35))
        products.listProducts.add(Product("Яйцо (желток)", 354))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CREATE_PRODUCT -> {
                    if (data != null) {
                        products.listProducts.add(
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
        products.setProducts()
    }

}