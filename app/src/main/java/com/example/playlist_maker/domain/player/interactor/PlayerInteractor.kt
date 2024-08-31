package com.example.playlist_maker.domain.player.interactor

import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.player.dto.PlayerState

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
    fun checkPlayerState(): Boolean
}

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {
    override fun preparePlayer(previewUrl: String) {
        return playerRepository.preparePlayer(previewUrl)
    }

    override fun applyState(state: PlayerState) {
        playerRepository.applyState(state)
    }

    override fun checkPlayerState(): Boolean {
        return playerRepository.checkPlayerState()
    }
}