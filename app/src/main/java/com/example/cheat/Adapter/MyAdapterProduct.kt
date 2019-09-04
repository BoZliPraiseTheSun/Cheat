package com.example.cheat.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat.Product
import com.example.cheat.R
import kotlinx.android.synthetic.main.view_holder_product.view.*

class MyAdapterProduct(
    private val list: ArrayList<Product>,
    val click: (Product) -> Unit
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
        val listProduct = list[position]
        holderProducts.apply {
            itemView.name_product_recycler.text = listProduct.name
            itemView.cal_product_text_recycler.text = listProduct.calorieContent.toString()

            itemView.setOnClickListener {
                click(listProduct)
            }
        }
    }

    class MyHolderProduct(itemView: View) : RecyclerView.ViewHolder(itemView)
}