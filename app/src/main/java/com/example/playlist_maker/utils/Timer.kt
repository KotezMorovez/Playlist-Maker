package com.example.playlist_maker.utils

import android.os.Handler
import android.os.Looper
import kotlin.math.max

class Timer(
    private val delay: Long,
    private val interval: Long,
    private val lambda: (Long) -> Unit
) {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var timeLeft = interval
    private var remainTime = 0L
    private var runnable: Runnable? = null

    var state: State = State.IDLE
        private set

    fun run() {
        if (state == State.IDLE) {
            start()
        } else if (state == State.PAUSED) {
            resume()
        }
    }

    fun reset() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
        state = State.IDLE
    }

    fun pause() {
        if (runnable != null) {
            handler.removeCallbacks(runnable!!)
        }

        timeLeft = remainTime - (System.currentTimeMillis() - startTime)

        state = State.PAUSED
    }

    private fun start() {
        startTime = System.currentTimeMillis()
        timeLeft = interval

        runnable = updateTimer(interval)
        handler.post(runnable!!)

        state = State.RUNNING
    }

    private fun resume() {
        startTime = System.currentTimeMillis()

        runnable = updateTimer(timeLeft)
        handler.postDelayed(runnable!!, timeLeft % 1000)

        state = State.RUNNING
    }

    private fun updateTimer(remainTime: Long): Runnable {
        this.remainTime = remainTime

        return object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                timeLeft = remainTime - elapsedTime
                val currentTimeLeft = timeLeft

                if (currentTimeLeft > 0) {
                    handler.postDelayed(this, delay)
                } else {
                    reset()
                }
                lambda.invoke(max(currentTimeLeft, 0))
            }
        }
    }

    enum class State {
        IDLE,
        RUNNING,
        PAUSED
    }
}