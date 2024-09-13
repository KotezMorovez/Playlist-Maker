package com.example.playlist_maker.data.player.repository

import com.example.playlist_maker.data.player.service.PlayerService
import com.example.playlist_maker.data.player.dto.toEntity
import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository

class PlayerRepositoryImpl(
    private val audioPlayerService: PlayerService
): PlayerRepository {
    override fun preparePlayer(previewUrl: String){
        audioPlayerService.preparePlayer(previewUrl)
    }

    override fun applyState(state: PlayerState) {
        audioPlayerService.applyState(state.toEntity())
    }

    override fun isStatePrepared(): Boolean {
        return audioPlayerService.isStatePrepared()
    }
}