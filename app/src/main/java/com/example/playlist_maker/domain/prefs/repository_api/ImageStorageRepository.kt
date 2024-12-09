package com.example.playlist_maker.domain.prefs.repository_api

import android.net.Uri

interface ImageStorageRepository {
    fun addImageToStorage(uri: Uri): Uri?
}