package com.example.playlist_maker.presentation.playlist_create.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.interactor.LibraryInteractor
import com.example.playlist_maker.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.util.UUID

class CreatePlaylistViewModel(
    private val playlistInteractor: LibraryInteractor
) : ViewModel() {
    private var _currentPlaylistCover: MutableLiveData<String> = MutableLiveData()
    val currentPlaylistCover: LiveData<String> = _currentPlaylistCover

    private var _creationEvent = SingleLiveEvent<String>()
    val creationEvent: LiveData<String> = _creationEvent

    fun saveImageUri(uri: Uri) {
        val newUri = playlistInteractor.addImageToStorage(uri)

        if (newUri != null) {
            _currentPlaylistCover.value = newUri.toString()
        }
    }

    fun savePlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistInteractor.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    imageUri = _currentPlaylistCover.value.toString(),
                    name = name,
                    description = description,
                    tracksCount = 0
                )
            )
        }
        _creationEvent.value = name
    }
}