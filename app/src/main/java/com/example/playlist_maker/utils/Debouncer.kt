package com.example.playlist_maker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delay: Long,
    coroutineScope: CoroutineScope,
    isDebouncer: Boolean,
    listener: (T) -> Unit
): (T) -> Unit {
    var job: Job? = null

    return { it: T ->
        if (isDebouncer) {
            job?.cancel()
        }

        if (isDebouncer || job?.isCompleted != false) {
            job = coroutineScope.launch {
                delay(delay)
                listener.invoke(it)
            }
        }
    }
}