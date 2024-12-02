package com.example.playlist_maker.presentation.playlist_create.view_model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.interactor.LibraryInteractor
import com.example.playlist_maker.utils.BitmapUtils
import kotlinx.coroutines.launch
import java.util.UUID

class CreatePlaylistViewModel(
    private val playlistInteractor: LibraryInteractor
) : ViewModel() {
    private var _currentPlaylistCover: MutableLiveData<Bitmap> = MutableLiveData()
    val currentPlaylistCover: LiveData<Bitmap> = _currentPlaylistCover
    private var uri: String = ""

    fun uploadImage(uri: Uri, contentResolver: ContentResolver) {
        this.uri = uri.toString()

        viewModelScope.launch {
            _currentPlaylistCover.value = BitmapUtils.getBitmapFromUri(uri, contentResolver)
        }
    }

    fun savePlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistInteractor.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    imageUri = uri,
                    name = name,
                    description = description,
                    tracksId = listOf()
                )
            )
        }
    }
}