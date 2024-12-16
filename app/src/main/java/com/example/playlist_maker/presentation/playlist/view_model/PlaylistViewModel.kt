package com.example.playlist_maker.presentation.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.interactor.PlaylistInteractor
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.playlist.dto.PlaylistUI
import com.example.playlist_maker.presentation.playlist.dto.toPlaylistUI
import com.example.playlist_maker.presentation.search.dto.TrackItem
import com.example.playlist_maker.presentation.search.dto.toUI
import com.example.playlist_maker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var playlistDomain: Playlist? = null
    private var trackListDomain: List<Track> = listOf()
    private var playlistSubscription: Job? = null

    private var _state: MutableLiveData<State> = MutableLiveData(State())
    val state: LiveData<State> = _state

    private val _navigationTrackEvent = SingleLiveEvent<Track>()
    val navigationTrackEvent: LiveData<Track>
        get() = _navigationTrackEvent

    private val _navigationEditEvent = SingleLiveEvent<String>()
    val navigationEditEvent: LiveData<String>
        get() = _navigationEditEvent

    private val _shareEvent = SingleLiveEvent<Boolean>()
    val shareEvent: LiveData<Boolean>
        get() = _shareEvent

    private val _deleteEvent = SingleLiveEvent<Unit>()
    val deleteEvent: LiveData<Unit>
        get() = _deleteEvent

    fun initialize(id: String) {
        playlistSubscription = viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect { playlistToTracks ->
                playlistDomain = playlistToTracks.first
                trackListDomain = playlistToTracks.second

                _state.value = State(
                    playlistToTracks.first.toPlaylistUI(),
                    playlistToTracks.second.map { it.toUI() }
                )
            }
        }
    }

    fun handleItemClick(item: TrackItem) {
        val track: Track = trackListDomain.firstOrNull { it.trackId == item.trackId } ?: return
        _navigationTrackEvent.value = track
    }

    fun handleShareButtonClick() {
        _shareEvent.value = trackListDomain.isNotEmpty()
    }

    fun handleEditButtonClick() {
        if (playlistDomain?.id != null) {
            _navigationEditEvent.value = playlistDomain!!.id
        }
    }

    fun getTracksText(): String {
        var result = ""

        for (i in 0..<trackListDomain.size) {
            val trackDuration = trackListDomain[i].toUI().trackTime
            result += "${i + 1}. ${trackListDomain[i].artistName} - ${trackListDomain[i].trackName} ($trackDuration)\n"
        }

        return result
    }

    fun deleteTrack(id: String) {
        if (playlistDomain != null) {
            viewModelScope.launch {
                playlistInteractor.deleteTrackFromPlaylist(
                    trackId = id,
                    playlistId = playlistDomain!!.id
                )
            }
        }
    }

    fun deletePlaylist(){
        viewModelScope.launch {
            playlistDomain.let {
                playlistSubscription?.cancel()
                playlistInteractor.deletePlaylist(playlistDomain!!.id)
            }
            _deleteEvent.call()
        }
    }

    data class State(
        val playlist: PlaylistUI = PlaylistUI(
            imageUri = "",
            name = "",
            description = "",
            tracksCount = 0,
            totalDuration = 0
        ),
        val tracks: List<TrackItem> = listOf()
    )
}