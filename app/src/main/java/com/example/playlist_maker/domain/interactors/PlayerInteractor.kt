package com.example.playlist_maker.domain.interactors

import com.example.playlist_maker.domain.model.PlayerState
import com.example.playlist_maker.domain.repository_api.PlayerRepository

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
}

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {
    override fun preparePlayer(previewUrl: String) {
        playerRepository.preparePlayer(previewUrl)
    }

    override fun applyState(state: PlayerState) {
        playerRepository.applyState(state)
    }
}