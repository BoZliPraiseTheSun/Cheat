package com.example.cheat

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_products.*
import kotlinx.android.synthetic.main.activity_list_products.view.*
import kotlinx.android.synthetic.main.list_product_view.*

class ListProductsActivity : AppCompatActivity() {


    var calIn100Gram = 0
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

        product_list_recycler.layoutManager = GridLayoutManager(this, 4)


    }

    override fun onResume() {
        super.onResume()
        product_list_recycler.adapter = MyAdapter(listProducts, image_product, product_name, add_product)
    }
}

class MyAdapter(
    private val list: ArrayList<Product>,
    val imageProduct: ImageView,
    val nameProduct: TextView,
    val layout: ConstraintLayout
) : RecyclerView.Adapter<MyAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_product_view, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItem(list[position], imageProduct, nameProduct, layout)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(
            listProduct: Product,
            imageProduct: ImageView,
            nameProduct: TextView,
            layout: ConstraintLayout
        ) {
            val imageView = itemView.findViewById<ImageView>(R.id.image_product_recycler)
            val textView = itemView.findViewById<TextView>(R.id.cal_product_text_recycler)

            imageView.setImageResource(listProduct.image)
            textView.text = listProduct.calorieContent.toString()

            itemView.setOnClickListener {
                Log.d(TAG, "click ${listProduct.name}")
                Log.d(TAG, "click ${listProduct.image}")
                ListProductsActivity().calIn100Gram = listProduct.calorieContent
                imageProduct.setImageResource(listProduct.image)
                nameProduct.text = listProduct.name
                layout.visibility = View.VISIBLE
            }
        }
    }
}

