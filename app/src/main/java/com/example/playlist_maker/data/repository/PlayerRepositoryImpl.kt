package com.example.playlist_maker.data.repository

import com.example.playlist_maker.data.mapper.toEntity
import com.example.playlist_maker.data.service.PlayerService
import com.example.playlist_maker.domain.model.PlayerState
import com.example.playlist_maker.domain.repository_api.PlayerRepository

class PlayerRepositoryImpl(
    private val audioPlayerService: PlayerService
): PlayerRepository {
    override fun preparePlayer(previewUrl: String) {
        audioPlayerService.preparePlayer(previewUrl)
    }

    override fun applyState(state: PlayerState) {
        audioPlayerService.applyState(state.toEntity())
    }
}