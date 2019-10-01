package com.example.cheat.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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


class ProductsStoreActivity : MvpAppCompatActivity(), ProductStoreView {

    companion object {
        const val FOOD_ADDED = 377
    }


    @InjectPresenter
    lateinit var productStorePresenter: ProductStorePresenter

    private val TAG = "ProductsStoreActivity"

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MyAdapterProduct

    private val listFoods: ArrayList<Foods> = arrayListOf()


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

        mAdapter()
        product_list_recycler.adapter = mAdapter


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FOOD_ADDED -> {
                    search_product.text.clear()
                    listFoods.clear()
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun mAdapter() {
        mAdapter = MyAdapterProduct(
            listFoods
        ) { food ->
            val intent = Intent(this, AddEatActivity::class.java)
            intent.putExtra("name", food.label)
            intent.putExtra("calories", food.nutrients.enerjy.roundToInt())
            startActivityForResult(intent, FOOD_ADDED)
        }
    }

    override fun refreshListFoods(list: ArrayList<Foods>) {
        listFoods.clear()
        listFoods.addAll(list)
        mAdapter.notifyDataSetChanged()

    }


}