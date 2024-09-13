package com.example.playlist_maker.data.player.service

import android.media.MediaPlayer
import com.example.playlist_maker.data.player.dto.PlayerStateEntity

interface PlayerService {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerStateEntity)
    fun isStatePrepared(): Boolean
}

class PlayerServiceImpl : PlayerService {
    private val player = MediaPlayer()
    private var isPrepared = false
    private var isDataSourceSet = false

    override fun preparePlayer(previewUrl: String) {
        if (!isPrepared && !isDataSourceSet) {
            isDataSourceSet = true

            player.setDataSource(previewUrl)
            player.prepareAsync()
            player.setOnPreparedListener {
                applyState(PlayerStateEntity.PREPARED)
            }
            player.setOnCompletionListener {
                applyState(PlayerStateEntity.PREPARED)
            }
        }
    }

    override fun applyState(state: PlayerStateEntity) {
        when (state) {
            PlayerStateEntity.PREPARED -> {
                isPrepared = true
            }

            PlayerStateEntity.STARTED -> {
                player.start()
            }

            PlayerStateEntity.PAUSED -> {
                player.pause()
            }

            PlayerStateEntity.RELEASED -> {
                isPrepared = false
                player.release()
            }
        }
    }

    override fun isStatePrepared(): Boolean {
        return isPrepared
    }
}