package com.example.cheat.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.cheat.*
import com.example.cheat.Presenter.ProductStorePresenter
import com.example.cheat.adapter.MyAdapterProduct
import com.example.cheat.view.ProductStoreView
import kotlinx.android.synthetic.main.activity_products_store.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ProductsStoreActivity : MvpAppCompatActivity(), ProductStoreView {

    companion object {
        lateinit var products: ListProducts
    }

    @InjectPresenter
    lateinit var productStorePresenter: ProductStorePresenter

    private val TAG = "ProductsStoreActivity"

    var calIn100Gram = -1

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapterProduct


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_store)

        mSettings =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)


        search_product_btn.setOnClickListener {
            val text = search_product.text.toString()
            productStorePresenter.getProducts(text)
        }


        layoutManager = LinearLayoutManager(this)
        product_list_recycler.layoutManager = layoutManager


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }


    override fun addAdapter(list: ArrayList<Foods>) {
        mAdapter = MyAdapterProduct(
            list
        ) { food ->
            val intent = Intent(this, ProductAddEatActivity::class.java)
            intent.putExtra("name", food.label)
            intent.putExtra("calories", food.nutrients.enerjy.roundToInt())
            startActivity(intent)
        }
    }

    override fun addAdapterInRecycler() {
        product_list_recycler.adapter = mAdapter
    }

}