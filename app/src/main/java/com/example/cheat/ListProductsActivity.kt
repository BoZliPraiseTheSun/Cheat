package com.example.cheat

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_products_list.*
import kotlin.math.roundToInt

class ListProductsActivity : AppCompatActivity() {

    companion object {
        var calIn100Gram = -1
    }
    lateinit var listProducts: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)
        listProducts = arrayListOf()
        listProducts.add(Product(R.drawable.ic_bulgur, "Булгур", 342))
        listProducts.add(Product(R.drawable.ic_chicken_fillet, "Куриная грудка", 150))
        listProducts.add(Product(R.drawable.ic_cream_cheese, "Сливочный сыр", 230))
        listProducts.add(Product(R.drawable.ic_ogyrez, "Огурец", 15))
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
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))
        listProducts.add(Product(R.drawable.ic_oatmeal, "Овсянка", 345))

        product_list_recycler.layoutManager = GridLayoutManager(this, 4)

        put_cal.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (put_cal.text.isNotEmpty()) {
                    gram_to_cal_text.text = ((calIn100Gram * put_cal.text.toString().toInt()) / 100f).roundToInt().toString()
                } else {
                    gram_to_cal_text.text = "0"
                }
            }

        })

        add_product_btn.setOnClickListener {
            add_product.visibility = View.GONE
            put_cal.clearFocus()
            put_cal.setText("")
            val handler = Handler()
            Thread(Runnable {
                try { Thread.sleep(5) } catch (e: InterruptedException) { Log.d(TAG, "Scroll error") }
                handler.post { scroll_view.fullScroll(View.FOCUS_UP) }
            }).start()
        }
    }

    override fun onResume() {
        super.onResume()
        product_list_recycler.adapter = MyAdapter(listProducts, image_product, product_name, add_product, put_cal, gram_to_cal_text, scroll_view)
    }
}


class MyAdapter(
    private val list: ArrayList<Product>,
    val imageProduct: ImageView,
    val nameProduct: TextView,
    val layout: ConstraintLayout,
    val putCal: EditText,
    val gramToCal: TextView,
    val scrollView: ScrollView
) : RecyclerView.Adapter<MyAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_product_view, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItem(list[position], imageProduct, nameProduct, layout, putCal, gramToCal, scrollView)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(
            listProduct: Product,
            imageProduct: ImageView,
            nameProduct: TextView,
            layout: ConstraintLayout,
            putCal: EditText,
            gramToCal: TextView,
            scrollView: ScrollView
        ) {
            val imageView = itemView.findViewById<ImageView>(R.id.image_product_recycler)
            val textView = itemView.findViewById<TextView>(R.id.cal_product_text_recycler)

            imageView.setImageResource(listProduct.image)
            textView.text = listProduct.calorieContent.toString()

            itemView.setOnClickListener {
                Log.d(TAG, "click ${listProduct.name}")
                Log.d(TAG, "click ${listProduct.image}")
                ListProductsActivity.calIn100Gram = listProduct.calorieContent
                imageProduct.setImageResource(listProduct.image)
                nameProduct.text = listProduct.name
                if (putCal.text.isNotEmpty()) {
                    gramToCal.text = ((listProduct.calorieContent * putCal.text.toString().toInt()) / 100f).roundToInt().toString()
                }
                if (layout.visibility == View.GONE) {
                    layout.visibility = View.VISIBLE
                }
                val handler = Handler()
                Thread(Runnable {
                    try { Thread.sleep(5) } catch (e: InterruptedException) { }
                    handler.post { scrollView.fullScroll(View.FOCUS_UP) }
                }).start()
            }
        }
    }
}

