package com.example.playlist_maker.ui.common

import android.os.Handler
import android.os.Looper

class Debouncer(
    private val delay: Long,
    private val listener: () -> Unit
) {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable { listener.invoke() }

    fun updateValue() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable , delay)
    }
}