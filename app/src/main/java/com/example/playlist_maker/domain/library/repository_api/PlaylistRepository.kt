package com.example.playlist_maker.domain.library.repository_api

import com.example.playlist_maker.domain.library.dto.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylistsSubscription(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
}