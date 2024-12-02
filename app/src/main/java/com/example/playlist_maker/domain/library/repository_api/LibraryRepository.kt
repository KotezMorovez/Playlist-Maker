package com.example.playlist_maker.domain.library.repository_api

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun getTracksSubscribtion(): Flow<List<Track>>
    suspend fun savePlaylist(playlist: Playlist)
}