package com.example.cheat.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.cheat.*
import com.example.cheat.presenter.ProductStorePresenter
import com.example.cheat.adapter.MyAdapterProduct
import com.example.cheat.retrofitModel.Foods
import com.example.cheat.view.ProductStoreView
import kotlinx.android.synthetic.main.activity_products_store.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ProductsStoreActivity : MvpAppCompatActivity(), ProductStoreView {

    @InjectPresenter
    lateinit var productStorePresenter: ProductStorePresenter

    private val TAG = "ProductsStoreActivity"

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapterProduct


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_store)


        search_product_btn.setOnClickListener {
            if (search_product.text.isNotEmpty()) {
                val text = search_product.text.toString()
                productStorePresenter.getProducts(text)
            }
            search_product.onEditorAction(EditorInfo.IME_ACTION_DONE)
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


    override fun addAdapter(list: ArrayList<Foods>) {
        mAdapter = MyAdapterProduct(
            list
        ) { food ->
            val intent = Intent(this, AddEatActivity::class.java)
            intent.putExtra("name", food.label)
            intent.putExtra("calories", food.nutrients.enerjy.roundToInt())
            startActivity(intent)
        }
    }

    override fun addAdapterInRecycler() {
        product_list_recycler.adapter = mAdapter
    }

}