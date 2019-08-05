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
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
