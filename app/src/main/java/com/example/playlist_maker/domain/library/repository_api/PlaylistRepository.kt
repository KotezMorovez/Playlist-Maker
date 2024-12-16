package com.example.playlist_maker.domain.library.repository_api

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylistsSubscription(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun getPlaylistByIdSubscription(id: String): Flow<Pair<Playlist, List<Track>>>
    suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: String)
    suspend fun deletePlaylist(id: String)
}