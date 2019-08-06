package com.example.cheat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soundcloud.android.crop.Crop
import kotlinx.android.synthetic.main.activity_add_new_product.*

class AddNewProduct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_product)

        val uri = intent.getParcelableExtra<Uri>("uri")
        image_from_camera.setImageURI(uri)
        test_btn.setOnClickListener {
            Crop.of(uri, uri).asSquare().start(this)
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
