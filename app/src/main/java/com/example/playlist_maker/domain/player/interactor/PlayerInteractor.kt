package com.example.playlist_maker.domain.player.interactor

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.PlaylistRepository
import com.example.playlist_maker.domain.library.repository_api.TrackRepository
import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
    fun isStatePrepared(): Boolean
    suspend fun loadPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToFavourite(track: Track)
    suspend fun deleteTrackFromFavourite(track: Track)
    suspend fun isTrackInFavourite(id: String): Boolean
    suspend fun tryToAddTrackToPlaylist(playlistId: String, track: Track): Boolean
}

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository,
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository
) : PlayerInteractor {
    override fun preparePlayer(previewUrl: String) {
        return playerRepository.preparePlayer(previewUrl)
    }

    override fun applyState(state: PlayerState) {
        playerRepository.applyState(state)
    }

    override fun isStatePrepared(): Boolean {
        return playerRepository.isStatePrepared()
    }

    override suspend fun loadPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistsSubscription().map {
            it
        }
    }

    override suspend fun addTrackToFavourite(track: Track) {
        trackRepository.addTrackToFavourite(track)
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        trackRepository.deleteTrackFromFavourite(track)
    }

    override suspend fun isTrackInFavourite(id: String): Boolean {
        return trackRepository.isTrackInFavourite(id)
    }

    override suspend fun tryToAddTrackToPlaylist(playlistId: String, track: Track): Boolean {
        val result = trackRepository.isTrackInPlaylist(playlistId, track.trackId)
        if (result) {
            return false
        } else {
            trackRepository.addTrackToPlaylist(playlistId, track)
        }
        return true
    }
}