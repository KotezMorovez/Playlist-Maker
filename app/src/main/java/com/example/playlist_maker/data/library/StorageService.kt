package com.example.playlist_maker.data.library

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

interface StorageService {
    fun addImageToStorage(uri: Uri): Uri?
}

class StorageServiceImpl(private val context: Context) : StorageService {
    override fun addImageToStorage(uri: Uri): Uri? {
        val bitmap = uriToBitmap(uri) ?: return null

        return createStorageFile(bitmap)
    }

    private fun createStorageFile(bitmap: Bitmap): Uri {
        val root = context.filesDir.absolutePath + "/playlist_maker_image"
        val imageUri = root + "/" + UUID.randomUUID() + ".png"
        val dir = File(root)
        dir.mkdir()

        FileOutputStream(imageUri).use { out ->
            bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                out
            )
        }

        return imageUri.toUri()
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = context.contentResolver
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}