package com.example.playlist_maker.domain.prefs.interactor

import android.net.Uri
import com.example.playlist_maker.domain.prefs.repository_api.ImageStorageRepository

interface ImageInteractor{
    fun addImageToStorage(uri: Uri): Uri?
}

class ImageInteractorImpl(
    private val imageStorageRepository: ImageStorageRepository
):ImageInteractor{
    override fun addImageToStorage(uri: Uri): Uri? {
        return imageStorageRepository.addImageToStorage(uri)
    }
}