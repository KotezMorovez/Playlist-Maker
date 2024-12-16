package com.example.playlist_maker.domain.library.interactor

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.PlaylistRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: String): Flow<Pair<Playlist, List<Track>>>
    suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: String)
    suspend fun deletePlaylist(id: String)
}

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistsSubscription()
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        return playlistRepository.savePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: String): Flow<Pair<Playlist, List<Track>>> {
        return playlistRepository.getPlaylistByIdSubscription(id)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: String) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deletePlaylist(id: String) {
        playlistRepository.deletePlaylist(id)
    }
}