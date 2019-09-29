package com.example.cheat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat.model.FoodEaten
import com.example.cheat.R
import kotlinx.android.synthetic.main.view_holder_product_eat.view.*

class MyAdapterFoodsEaten(private val list: ArrayList<FoodEaten>, val click: (FoodEaten) -> Unit) :
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
        val foodEat = list[position]
        holderProductsEat.apply {
            itemView.name_eat_view.text = foodEat.name
            itemView.cal_eat_view.text = foodEat.calorieEat.toString()
            itemView.gram_eat_view.text = foodEat.gramsEat.toString()
            itemView.setOnClickListener {
                click(foodEat)
            }
        }
    }

    class MyHolderProductEat(itemView: View) : RecyclerView.ViewHolder(itemView)
}