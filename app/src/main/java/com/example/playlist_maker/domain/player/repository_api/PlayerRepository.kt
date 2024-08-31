package com.example.playlist_maker.domain.player.repository_api

import com.example.playlist_maker.domain.player.dto.PlayerState

interface PlayerRepository {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
    fun checkPlayerState(): Boolean
}