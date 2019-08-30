package com.example.cheat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_holder_product_eat.view.*

class MyAdapterFoodsEaten(private val list: ArrayList<FoodsEaten>) :
    RecyclerView.Adapter<MyAdapterFoodsEaten.MyHolderProductEat>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderProductEat {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_holder_product_eat, parent, false)
        return MyHolderProductEat(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holderProductsEat: MyHolderProductEat, position: Int) {
        val listProductEat = list[position]
        holderProductsEat.apply {
            if (listProductEat.imageUri != null) {
                itemView.image_product_eat.setImageURI(listProductEat.imageUri.toUri())
            }
            itemView.name_eat_view.text = listProductEat.name
            itemView.cal_eat_view.text = listProductEat.calorieEat.toString()
            itemView.gram_eat_view.text = listProductEat.gramsEat.toString()
            itemView.setOnClickListener { }
        }
    }

    class MyHolderProductEat(itemView: View) : RecyclerView.ViewHolder(itemView)
}