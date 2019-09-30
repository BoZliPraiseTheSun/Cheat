package com.example.cheat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat.retrofitModel.infoFood
import com.example.cheat.retrofitModel.Foods
import com.example.cheat.R
import kotlinx.android.synthetic.main.view_holder_product.view.*
import kotlin.math.roundToInt

class MyAdapterProduct(
    private val list: ArrayList<Foods>,
    val click: (infoFood) -> Unit
) : RecyclerView.Adapter<MyAdapterProduct.MyHolderProduct>() {

    val TAG = "ActivityAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderProduct {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_product, parent, false)
        return MyHolderProduct(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holderProducts: MyHolderProduct, position: Int) {
        val food = list[position]
        holderProducts.apply {
            itemView.name_product_recycler.text = food.infoFood.label
            itemView.cal_product_text_recycler.text = food.infoFood.nutrients.enerjy.roundToInt().toString()
            itemView.setOnClickListener {
                click(food.infoFood)
            }
        }
    }

    class MyHolderProduct(itemView: View) : RecyclerView.ViewHolder(itemView)
}