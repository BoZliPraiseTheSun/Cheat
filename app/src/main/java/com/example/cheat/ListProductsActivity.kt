package com.example.cheat

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_products.*

class ListProductsActivity : AppCompatActivity() {

    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var listProducts: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_products)

        listProducts = arrayListOf()
        listProducts.add(Product(R.drawable.ic_bulgur, "Булгур", 342))
        listProducts.add(Product(R.drawable.ic_chicken_fillet, "Куриная грудка", 150))
        listProducts.add(Product(R.drawable.ic_cream_cheese, "Сливочный сыр", 230))
        listProducts.add(Product(R.drawable.ic_ogyrez, "Огурац", 15))
        listProducts.add(Product(R.drawable.ic_apple, "Яблоко", 47))
        listProducts.add(Product(R.drawable.ic_zucchini, "Кабачёк", 27))
        listProducts.add(Product(R.drawable.ic_nectarine, "Нектарин", 44))
        listProducts.add(Product(R.drawable.ic_cherries, "Черешня", 52))
        listProducts.add(Product(R.drawable.ic_strawberry, "Клубника", 33))
        listProducts.add(Product(R.drawable.ic_tomato, "Помидор", 24))
        listProducts.add(Product(R.drawable.ic_buckwheat, "Гречка", 326))
        listProducts.add(Product(R.drawable.ic_banana, "Банан", 91))
        listProducts.add(Product(R.drawable.ic_potato, "Картошка", 83))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))

        layoutManager = GridLayoutManager(this, 4)
        product_list_recycler.layoutManager = layoutManager
        product_list_recycler.adapter = MyAdapter(listProducts)
    }
}

class MyAdapter(val list: ArrayList<Product>): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_product_view, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Log.d(TAG, "position")
        holder.bindItem(list[position])
    }

    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(listProduct: Product) {
            Log.d(TAG, "holder")
            val imageView = itemView.findViewById<ImageView>(R.id.image_product)
            val textView = itemView.findViewById<TextView>(R.id.cal_product_text)

            imageView.setImageResource(listProduct.image)
            textView.text = listProduct.calorieContent.toString()


        }

    }
}
