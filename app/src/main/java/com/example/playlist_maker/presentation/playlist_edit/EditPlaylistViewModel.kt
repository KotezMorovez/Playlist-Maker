package com.example.playlist_maker.presentation.playlist_edit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.interactor.PlaylistInteractor
import com.example.playlist_maker.domain.prefs.interactor.ImageInteractor
import com.example.playlist_maker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val coverInteractor: ImageInteractor
) : ViewModel() {
    private var playlistId: String = ""
    private var trackCount: Int = 0
    private var _currentPlaylistCover: MutableLiveData<String> = MutableLiveData()
    val currentPlaylistCover: LiveData<String> = _currentPlaylistCover

    private var _playlistInfoEvent = SingleLiveEvent<State>()
    val playlistInfoEvent: LiveData<State> = _playlistInfoEvent

    private var _creationEvent = SingleLiveEvent<String>()
    val creationEvent: LiveData<String> = _creationEvent

    fun initialize(id: String) {
        playlistId = id
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect { pair ->
                _playlistInfoEvent.value = State(
                    name = pair.first.name,
                    description = pair.first.description
                )
                _currentPlaylistCover.value = pair.first.imageUri
                trackCount = pair.second.size
            }
        }
    }

    fun saveImageUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUri = coverInteractor.addImageToStorage(uri)

            if (newUri != null) {
                withContext(Dispatchers.Main) {
                    _currentPlaylistCover.value = newUri.toString()
                }
            }
        }
    }

    fun savePlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistInteractor.savePlaylist(
                Playlist(
                    id = playlistId,
                    imageUri = _currentPlaylistCover.value ?: "",
                    name = name,
                    description = description,
                    tracksCount = trackCount,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        _creationEvent.value = name
    }

    data class State(
        val name: String,
        val description: String
    )
}