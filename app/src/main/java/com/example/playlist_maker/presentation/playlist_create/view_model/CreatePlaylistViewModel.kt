package com.example.playlist_maker.presentation.playlist_create.view_model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.playlist.interactor.PlaylistInteractor
import com.example.playlist_maker.utils.BitmapUtils
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var _currentPlaylistCover: MutableLiveData<Bitmap> = MutableLiveData()
    val currentPlaylistCover: LiveData<Bitmap> = _currentPlaylistCover

    fun uploadImage(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            _currentPlaylistCover.value = BitmapUtils.getBitmapFromUri(uri, contentResolver)
        }
    }

    fun savePlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistInteractor.savePlaylist(_currentPlaylistCover.value, name, description)
        }
    }
}