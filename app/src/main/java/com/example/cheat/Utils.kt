package com.example.cheat

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ScrollView

fun slowScroll(scroll_view: ScrollView, TAG: String) {
    val handler = Handler()
    Thread(Runnable {
        try {
            Thread.sleep(1)
        } catch (e: InterruptedException) {
            Log.d(TAG, "Scroll error")
        }
        handler.post { scroll_view.fullScroll(View.FOCUS_UP) }
    }).start()
}