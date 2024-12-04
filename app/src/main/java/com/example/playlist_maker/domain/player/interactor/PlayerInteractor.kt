package com.example.playlist_maker.domain.player.interactor

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
    fun isStatePrepared(): Boolean
    fun loadPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToFavourite(track: Track)
    suspend fun deleteTrackFromFavourite(track: Track)
    suspend fun isTrackInFavourite(id: String): Boolean
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

    override fun isStatePrepared(): Boolean {
        return playerRepository.isStatePrepared()
    }

    override fun loadPlaylists(): Flow<List<Playlist>> {
        return playerRepository.loadPlaylists()
    }

    override suspend fun addTrackToFavourite(track: Track) {
        playerRepository.addTrackToFavourite(track)
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        playerRepository.deleteTrackFromFavourite(track)
    }

    override suspend fun isTrackInFavourite(id: String): Boolean {
        return playerRepository.isTrackInFavourite(id)
    }
}