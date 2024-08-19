package com.example.playlist_maker.data.service

import android.media.MediaPlayer
import com.example.playlist_maker.data.model.PlayerStateEntity

interface PlayerService{
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerStateEntity)
}

class PlayerServiceImpl: PlayerService {
    private val player = MediaPlayer()

    override fun preparePlayer(previewUrl: String) {
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            applyState(PlayerStateEntity.PREPARED)
        }

        player.setOnCompletionListener {
            applyState(PlayerStateEntity.PREPARED)
        }
    }

    override fun applyState(state: PlayerStateEntity) {
        when (state) {
            PlayerStateEntity.PREPARED -> {}

            PlayerStateEntity.STARTED -> {
                player.start()
            }

            PlayerStateEntity.PAUSED -> {
                player.pause()
            }

            PlayerStateEntity.RELEASED -> {
                player.release()
            }
        }
    }
}