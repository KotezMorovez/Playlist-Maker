package com.example.playlist_maker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Timer(
    private val delay: Long,
    private val totalTime: Long,
    private val coroutineScope: CoroutineScope,
    private val lambda: (Long) -> Unit
) {
    private var startTimestamp = 0L
    private var lastPauseTimestamp = 0L
    private var job: Job? = null
    private var remainTime = totalTime
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
        state = State.IDLE
        job?.cancel()
    }

    fun pause() {
        state = State.PAUSED
        lastPauseTimestamp = System.currentTimeMillis()
        job?.cancel()
    }

    private fun start() {
        state = State.RUNNING
        remainTime = totalTime
        startTimestamp = System.currentTimeMillis()
        lambda.invoke(remainTime)
        startJob()
    }

    private fun resume() {
        state = State.RUNNING
        startJob()
    }

    private fun startJob() {
        job = coroutineScope.launch {
            while (state == State.RUNNING) {
                val currentDelay = if ((lastPauseTimestamp - startTimestamp) % delay > 0) {
                    val newDelay = delay - (lastPauseTimestamp - startTimestamp) % delay
                    lastPauseTimestamp = startTimestamp
                    newDelay
                } else {
                    delay
                }
                if (remainTime > 0) {
                    delay(currentDelay)
                    remainTime -= currentDelay
                } else {
                    reset()
                }

                lambda.invoke(remainTime)
            }
        }
    }

    enum class State {
        IDLE,
        RUNNING,
        PAUSED
    }
}