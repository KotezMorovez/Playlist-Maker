package com.example.playlist_maker.domain.library.repository_api

import android.net.Uri
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun getTracksSubscription(): Flow<List<Track>>
    suspend fun getPlaylistsSubscription(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    fun addImageToStorage(uri: Uri): Uri?
}