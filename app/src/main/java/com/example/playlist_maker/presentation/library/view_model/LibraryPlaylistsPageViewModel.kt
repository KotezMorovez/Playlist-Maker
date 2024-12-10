package com.example.playlist_maker.presentation.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.interactor.PlaylistInteractor
import com.example.playlist_maker.presentation.library.dto.PlaylistLibraryItem
import com.example.playlist_maker.presentation.library.dto.toPlaylistLibraryUI
import com.example.playlist_maker.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LibraryPlaylistsPageViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var playlistDomainList: List<Playlist> = listOf()

    private var _currentState: MutableLiveData<State> = MutableLiveData(State.NoData)
    val currentState: LiveData<State>
        get() = _currentState

    private val _navigationEvent = SingleLiveEvent<Playlist>()
    val navigationEvent: LiveData<Playlist>
        get() = _navigationEvent


    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                playlistDomainList = it

                if (playlistDomainList.isNotEmpty()) {
                    _currentState.value = State.Data(playlistDomainList.map { playlist ->
                        playlist.toPlaylistLibraryUI()
                    })
                }
            }
        }
    }

    fun handleItemClick(item: PlaylistLibraryItem) { /*TODO*/ }

    sealed class State {
        class Data(val list: List<PlaylistLibraryItem>) : State()
        data object NoData : State()
    }
}