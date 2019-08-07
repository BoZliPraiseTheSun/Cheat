package com.example.cheat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_products_list.*
import kotlinx.android.synthetic.main.list_product_view.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ListProductsActivity : AppCompatActivity() {

    companion object {
        var calIn100Gram = -1
        var idImage = ""
    }

    val REQUEST_CROP_PHOTO = 333
    val REQUEST_TAKE_PHOTO = 334
    val TAG = "ListProductsActivity"
    lateinit var listProducts: ArrayList<Product>
    lateinit var mImageUri: Uri
    lateinit var mSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)


        mSettings = getSharedPreferences(UserActivity.SETTINGS, Context.MODE_PRIVATE)

        listProducts = arrayListOf()

        product_list_recycler.layoutManager = GridLayoutManager(this, 4)

        put_cal.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (put_cal.text.isNotEmpty()) {
                    gram_to_cal_text.text =
                        ((calIn100Gram * put_cal.text.toString().toInt()) / 100f).roundToInt().toString()
                } else {
                    gram_to_cal_text.text = "0"
                }
            }
        })

        add_product_btn.setOnClickListener {
            add_product.visibility = View.GONE
            val calEat = mSettings.getInt(UserActivity.SETTINGS_CAL_EAT, 0)
            mSettings
                .edit()
                .putInt(UserActivity.SETTINGS_CAL_EAT, gram_to_cal_text.text.toString().toInt() + calEat)
                .apply()

            val listEat = UserActivity.listEat
            var createProduct = true
            if (listEat.isNotEmpty()) {
                Log.d(TAG, "listEat.add")
                for (i in listEat) {
                    if (i.name == product_name.text.toString()) {
                        i.calorieEat += gram_to_cal_text.text.toString().toInt()
                        i.gramsEat += put_cal.text.toString().toInt()
                        createProduct = false
                    }
                }
            }
            if (createProduct) {
                Log.d(TAG, "listEat.create")
                listEat.add(
                    ProductEat(
                        idImage,
                        product_name.text.toString(),
                        gram_to_cal_text.text.toString().toInt(),
                        put_cal.text.toString().toInt()
                    )
                )
            }

            put_cal.clearFocus()
            put_cal.setText("")
            slowScroll(scroll_view, TAG)
        }

        test_open_camera.setOnClickListener {
            dispatchTakePictureIntent()
        }
        getListProduct()
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFile = createImageFile()
        mImageUri = FileProvider.getUriForFile(
            this,
            "com.example.cheat.provider",
            imageFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CROP_PHOTO -> {
                    Log.d(TAG, "REQUEST_CROP_PHOTO")
                    if (data != null) {
                        listProducts.add(
                            Product(
                                mImageUri.toString(),
                                data.getStringExtra("Name")!!,
                                data.getIntExtra("Calorie Content", -1)
                            )
                        )
                        product_list_recycler.adapter =
                            MyAdapterProducts(
                                listProducts,
                                image_product,
                                product_name,
                                add_product,
                                put_cal,
                                gram_to_cal_text,
                                scroll_view
                            )
                    }
                }
                REQUEST_TAKE_PHOTO -> {
                    Log.d(TAG, "REQUEST_TAKE_PHOTO")
                    val intent = Intent(this, AddNewProduct::class.java)
                    intent.putExtra("uri", mImageUri)
                    Log.d(TAG, "start Activity AddNewProduct")
                    startActivityForResult(intent, REQUEST_CROP_PHOTO)
                }
            }
        }
    }

    fun getListProduct() {
        val gsonTextt = mSettings.getString(UserActivity.SETTINGS_LIST_PRODUCTS, "")
        if (gsonTextt != "") {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            listProducts.clear()
            listProducts.addAll(Gson().fromJson(gsonTextt, type))
        }
    }

    override fun onResume() {
        super.onResume()
        product_list_recycler.adapter =
            MyAdapterProducts(
                listProducts,
                image_product,
                product_name,
                add_product,
                put_cal,
                gram_to_cal_text,
                scroll_view
            )
    }

    override fun onPause() {
        super.onPause()
        val gsonText = Gson().toJson(listProducts)
        mSettings.edit().putString(UserActivity.SETTINGS_LIST_PRODUCTS, gsonText).apply()
    }

    class MyAdapterProducts(
        private val list: ArrayList<Product>,
        private val imageProduct: ImageView,
        private val nameProduct: TextView,
        private val layout: ConstraintLayout,
        private val putCal: EditText,
        private val gramToCal: TextView,
        private val scrollView: ScrollView
    ) : RecyclerView.Adapter<MyAdapterProducts.MyHolderProducts>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderProducts {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_product_view, parent, false)
            return MyHolderProducts(itemView)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holderProducts: MyHolderProducts, position: Int) {
            holderProducts.bindItem(list[position], imageProduct, nameProduct, layout, putCal, gramToCal, scrollView)
        }

        class MyHolderProducts(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val TAG = "ActivityHolder"
            fun bindItem(
                listProduct: Product,
                imageProduct: ImageView,
                nameProduct: TextView,
                layout: ConstraintLayout,
                putCal: EditText,
                gramToCal: TextView,
                scrollView: ScrollView
            ) {

                itemView.image_product_recycler.setImageURI(listProduct.imageUri.toUri())
                itemView.cal_product_text_recycler.text = listProduct.calorieContent.toString()

                itemView.setOnClickListener {
                    Log.d(TAG, "click ${listProduct.name}")
                    calIn100Gram = listProduct.calorieContent
                    idImage = listProduct.imageUri
                    imageProduct.setImageURI(listProduct.imageUri.toUri())
                    nameProduct.text = listProduct.name
                    if (putCal.text.isNotEmpty()) {
                        gramToCal.text =
                            ((listProduct.calorieContent * putCal.text.toString().toInt()) / 100f)
                                .roundToInt()
                                .toString()
                    }
                    if (layout.visibility == View.GONE) {
                        layout.visibility = View.VISIBLE
                    }
                    slowScroll(scrollView, TAG)
                }
            }
        }
    }

}