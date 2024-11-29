package com.example.playlist_maker.domain.playlist.interactor

import android.graphics.Bitmap
import com.example.playlist_maker.domain.library.repository_api.LibraryRepository

interface PlaylistInteractor {
    suspend fun savePlaylist(image: Bitmap?, name: String, description: String): Result<Unit>
}

class PlaylistInteractorImpl(
    private val playlistRepository: LibraryRepository
) : PlaylistInteractor {
    override suspend fun savePlaylist(
        image: Bitmap?,
        name: String,
        description: String
    ): Result<Unit> {
        //TODO("Not yet implemented")
        return Result.success(Unit)
    }
}