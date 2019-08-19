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
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_list_products.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ListProductsActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_TAKE_PHOTO = 334
        const val REQUEST_CROP_PHOTO = 333
        var calIn100Gram = -1
        var idImage = ""
    }

    private val TAG = "ListProductsActivity"

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mImageUri: Uri
    private lateinit var mAdapter: MyAdapterProduct

    private val listProducts: ArrayList<Product> = arrayListOf()
    private val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_products)


        mSettings = getSharedPreferences(UserActivity.SETTINGS, Context.MODE_PRIVATE)
        layoutManager = GridLayoutManager(this, 4)

        product_list_recycler.layoutManager = layoutManager

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
            put_cal.text.clear()
            slowScroll(scroll_view, TAG)
        }

        test_open_camera.setOnClickListener {
            dispatchTakePictureIntent()
        }
        
        getListProduct()
        createRecyclerView(listProducts)
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
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp.format(Date())}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CROP_PHOTO -> {
                    if (data != null) {
                        listProducts.add(
                            Product(
                                mImageUri.toString(),
                                data.getStringExtra("Name")!!,
                                data.getIntExtra("Calorie Content", -1)
                            )
                        )
                        mAdapter.notifyDataSetChanged()
                    }
                }
                REQUEST_TAKE_PHOTO -> {
                    val intent = Intent(this, AddNewProduct::class.java)
                    intent.putExtra("uri", mImageUri)
                    startActivityForResult(intent, REQUEST_CROP_PHOTO)
                }
            }
        }
    }

    private fun getListProduct() {
        val gsonText = mSettings.getString(UserActivity.SETTINGS_LIST_PRODUCTS, "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            listProducts.clear()
            listProducts.addAll(Gson().fromJson(gsonText, type))
        }
    }

    private fun createRecyclerView(list: ArrayList<Product>) {
        mAdapter = MyAdapterProduct(
            list
        ) { product ->
            calIn100Gram = product.calorieContent
            idImage = product.imageUri
            image_product.setImageURI(product.imageUri.toUri())
            product_name.text = product.name
            if (put_cal.text.isNotEmpty()) {
                gram_to_cal_text.text =
                    ((product.calorieContent * put_cal.text.toString().toInt()) / 100f)
                        .roundToInt()
                        .toString()
            }
            if (add_product.visibility == View.GONE) {
                add_product.visibility = View.VISIBLE
            }
            slowScroll(scroll_view, TAG)
        }
        product_list_recycler.adapter = mAdapter

    }

    override fun onPause() {
        super.onPause()
        val gsonText = Gson().toJson(listProducts)
        mSettings.edit().putString(UserActivity.SETTINGS_LIST_PRODUCTS, gsonText).apply()
    }

}