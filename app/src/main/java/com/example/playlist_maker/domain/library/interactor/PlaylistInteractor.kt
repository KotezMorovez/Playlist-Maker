package com.example.playlist_maker.domain.library.interactor

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.PlaylistRepository
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
}

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistsSubscription()
    }

    override suspend fun savePlaylist(playlist: Playlist
    ) {
        return playlistRepository.savePlaylist(playlist)
    }
}