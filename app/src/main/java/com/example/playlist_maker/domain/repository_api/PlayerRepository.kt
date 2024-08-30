package com.example.playlist_maker.domain.repository_api

import com.example.playlist_maker.domain.model.PlayerState

interface PlayerRepository {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
}