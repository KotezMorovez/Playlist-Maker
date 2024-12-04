package com.example.playlist_maker.domain.library.interactor

import android.net.Uri
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.LibraryRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LibraryInteractor {
    suspend fun getTracks(): Flow<List<Track>>
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(playlist: Playlist)
    fun addImageToStorage(uri: Uri): Uri?
}

class LibraryInteractorImpl(
    private val libraryRepository: LibraryRepository
):LibraryInteractor {
    override suspend fun getTracks(): Flow<List<Track>> {
        return libraryRepository.getTracksSubscription().map { list -> list.reversed() }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return libraryRepository.getPlaylistsSubscription().map { list -> list.reversed() }
    }

    override suspend fun savePlaylist(playlist: Playlist
    ) {
        return libraryRepository.savePlaylist(playlist)
    }

    override fun addImageToStorage(uri: Uri): Uri? {
        return libraryRepository.addImageToStorage(uri)
    }
}