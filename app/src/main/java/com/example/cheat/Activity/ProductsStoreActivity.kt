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
        const val REQUEST_ADD_PRODUCT = 337
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


        test_add_product.setOnClickListener {
            val intent = Intent(this, AddNewProductActivity::class.java)
            startActivityForResult(intent, REQUEST_CREATE_PRODUCT)
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



    private fun createRecyclerView(list: ArrayList<Product>) {
        mAdapter = MyAdapterProduct(
            list
        ) { product ->
            val intent = Intent(this, ProductAddEatActivity::class.java)
            intent.putExtra("calories", product.calorieContent)
            intent.putExtra("name", product.name)
            startActivity(intent)
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
                REQUEST_ADD_PRODUCT -> {

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        products.setProducts()
    }

}