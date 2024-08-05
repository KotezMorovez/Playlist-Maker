package com.example.playlist_maker.ui.common

import android.media.MediaPlayer
import android.util.Log
import com.example.playlist_maker.R

class AudioPlayer {
    private val player = MediaPlayer()

    fun preparePlayer(previewUrl: String) {
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            applyState(State.PREPARED)
        }

        player.setOnCompletionListener {
            applyState(State.PREPARED)
        }
    }

    fun applyState(state: State) {
        when (state) {
            State.PREPARED -> {
                Log.i(R.string.app_name.toString(), "Player is ready")
            }

            State.STARTED -> {
                player.start()
            }

            State.PAUSED -> {
                player.pause()
            }

            State.RELEASED -> {
                player.release()
            }
        }
    }

    enum class State {
        PREPARED,
        STARTED,
        PAUSED,
        RELEASED
    }
}