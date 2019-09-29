package com.example.cheat

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ScrollView

fun slowScroll(scrollView: ScrollView) {
    val handler = Handler()
    Thread(Runnable {
        try {
            Thread.sleep(1)
        } catch (e: InterruptedException) {
        }
        handler.post{ scrollView.fullScroll(View.FOCUS_UP) }
    }).start()
}

fun log(tag: String, massage: String) {
    Log.d(tag, massage)
}