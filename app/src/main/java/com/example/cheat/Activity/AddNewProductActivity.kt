package com.example.cheat.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cheat.R
import com.soundcloud.android.crop.Crop
import kotlinx.android.synthetic.main.activity_add_new_product.*

class AddNewProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_product)

        test_btn.setOnClickListener {
            val bundle = Intent()
            if (save_name_product.text.isNotEmpty() && save_calorie_content.text.isNotEmpty()) {
                bundle.putExtra("Name", save_name_product.text.toString())
                bundle.putExtra("Calorie Content", save_calorie_content.text.toString().toInt())
                setResult(Activity.RESULT_OK, bundle)
                finish()
            }
        }
    }
}
