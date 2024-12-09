package com.example.playlist_maker.data.storage.repository

import android.net.Uri
import com.example.playlist_maker.data.storage.service.ImageStorage
import com.example.playlist_maker.domain.prefs.repository_api.ImageStorageRepository

class ImageStorageRepositoryImpl(
    private val imageStorage: ImageStorage
): ImageStorageRepository {
    override fun addImageToStorage(uri: Uri): Uri? {
        return imageStorage.addImageToStorage(uri)
    }
}