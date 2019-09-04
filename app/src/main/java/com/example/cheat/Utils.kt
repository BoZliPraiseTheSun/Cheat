package com.example.cheat

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ScrollView
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

private val dataFormatDD = SimpleDateFormat("dd", Locale.UK)

fun getData(): Int {
    return dataFormatDD.format(Date()).toInt()
}

fun slowScroll(scroll_view: ScrollView, TAG: String) {
    val handler = Handler()
    Thread(Runnable {
        try {
            Thread.sleep(1)
        } catch (e: InterruptedException) {
            Log.d(TAG, "Scroll error")
        }
        handler.post{ scroll_view.fullScroll(View.FOCUS_UP) }
    }).start()
}