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
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_products_store.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ProductsStoreActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_TAKE_PHOTO = 334
        const val REQUEST_CREATE_PRODUCT = 333
    }

    private val TAG = "ProductsStoreActivity"

    var calIn100Gram = -1

    private lateinit var mSettings: SharedPreferences
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mImageUri: Uri
    private lateinit var mAdapter: MyAdapterProduct

    private val listProducts: ArrayList<Product> = arrayListOf()
    private val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_store)
        firstOpenApp()


        initializationLateInitParam()


        product_list_recycler.layoutManager = layoutManager

        put_cal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                view_cal_in_seek_bar.text = p1.toString()
                gram_to_cal_text.text = viewCal(calIn100Gram, p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        deleted_product.setOnClickListener {
            listProducts.remove(Product(product_name.text.toString(), calIn100Gram))
            add_product.visibility = View.GONE
            mAdapter.notifyDataSetChanged()

        }

        add_product_btn.setOnClickListener {
            add_product.visibility = View.GONE
            val calEat = mSettings.getInt(getString(R.string.cal_eat_key), 0)
            mSettings
                .edit()
                .putInt(
                    getString(R.string.cal_eat_key),
                    gram_to_cal_text.text.toString().toInt() + calEat
                )
                .apply()

            val listEat = UserActivity.listFoodsEaten
            var createProduct = true
            if (listEat.isNotEmpty()) {
                Log.d(TAG, "listFoodsEaten.add")
                for (i in listEat) {
                    if (i.name == product_name.text.toString()) {
                        i.calorieEat += gram_to_cal_text.text.toString().toInt()
                        i.gramsEat += put_cal.progress.toString().toInt()
                        createProduct = false
                    }
                }
            }
            if (createProduct) {
                Log.d(TAG, "listFoodsEaten.create")
                listEat.add(
                    FoodsEaten(
                        product_name.text.toString(),
                        gram_to_cal_text.text.toString().toInt(),
                        put_cal.progress.toString().toInt()
                    )
                )
            }

            put_cal.progress = 0
            slowScroll(scroll_view, TAG)
        }

        test_add_product.setOnClickListener {
            val intent = Intent(this, AddNewProductActivity::class.java)
            startActivity(intent)
        }

        getListProduct()
        createRecyclerView(listProducts)
    }

    private fun initializationLateInitParam() {
        mSettings =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(this)
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

    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp.format(Date())}_",
            ".jpg",
            storageDir
        )
    }

    private fun getListProduct() {
        val gsonText = mSettings.getString(getString(R.string.list_product_key), "")
        if (gsonText != "") {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            listProducts.clear()
            listProducts.addAll(Gson().fromJson(gsonText, type))
        }
    }

    private fun viewCal(calorieContent: Int, gram: Int): String {
        return ((calorieContent * put_cal.progress.toString().toInt()) / 100f)
            .roundToInt()
            .toString()
    }

    private fun firstOpenApp() {
        if (getData() == -1) {
            listProducts.add(Product("Булгур", 342))
            listProducts.add(Product("Яблоко", 47))
            listProducts.add(Product("Сливочный сыр", 210))
            listProducts.add(Product("Грудка куриная (мангал)", 175))
            listProducts.add(Product("Нектарин", 63))
            listProducts.add(Product("Борщ", 45))
            listProducts.add(Product("Огурец", 12))
            listProducts.add(Product("Помидор", 20))
            listProducts.add(Product("Яйцо (белок)", 48))
            listProducts.add(Product("Груша", 47))
            listProducts.add(Product("Мандарин", 38))
            listProducts.add(Product("Виноград", 72))
            listProducts.add(Product("Тунец (консервы)", 96))
            listProducts.add(Product("Яйцо", 157))
            listProducts.add(Product("Банан", 96))
            listProducts.add(Product("Кабачок", 24))
            listProducts.add(Product("Картошка", 77))
            listProducts.add(Product("Грудка куриная", 164))
            listProducts.add(Product("Морковь", 35))
            listProducts.add(Product("Яйцо (желток)", 354))
        }
    }

    private fun createRecyclerView(list: ArrayList<Product>) {
        mAdapter = MyAdapterProduct(
            list
        ) { product ->
            calIn100Gram = product.calorieContent
            product_name.text = product.name
            gram_to_cal_text.text = viewCal(product.calorieContent, put_cal.progress)
            if (add_product.visibility == View.GONE) {
                add_product.visibility = View.VISIBLE
            }
            slowScroll(scroll_view, TAG)
        }
        product_list_recycler.adapter = mAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CREATE_PRODUCT -> {
                    if (data != null) {
                        listProducts.add(
                            Product(
                                data.getStringExtra("Name")!!,
                                data.getIntExtra("Calorie Content", -1)
                            )
                        )
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val gsonText = Gson().toJson(listProducts)
        mSettings.edit().putString(getString(R.string.list_product_key), gsonText).apply()
    }

}