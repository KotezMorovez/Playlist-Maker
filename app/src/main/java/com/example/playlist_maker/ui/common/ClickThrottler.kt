package com.example.playlist_maker.ui.common

import android.os.Handler
import android.os.Looper

class ClickThrottler(
    private val delay: Long
) {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    fun clickThrottle(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, delay)
        }
        return current
    }
}