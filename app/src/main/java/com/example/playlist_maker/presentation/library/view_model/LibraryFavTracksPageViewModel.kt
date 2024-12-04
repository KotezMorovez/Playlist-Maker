package com.example.playlist_maker.presentation.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.library.interactor.LibraryInteractor
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.library.dto.TrackLibraryItem
import com.example.playlist_maker.presentation.library.dto.toTrackLibraryUI
import com.example.playlist_maker.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LibraryFavTracksPageViewModel(
    private val libraryInteractor: LibraryInteractor
) : ViewModel() {
    private var tracksDomainList: List<Track> = listOf()

    private var _currentState: MutableLiveData<State> = MutableLiveData(State.NoData)
    val currentState: LiveData<State>
        get() = _currentState

    private val _navigationEvent = SingleLiveEvent<Track>()
    val navigationEvent: LiveData<Track>
        get() = _navigationEvent

    init {
        viewModelScope.launch {
            libraryInteractor.getTracks().collect {
                tracksDomainList = it

                if (tracksDomainList.isNotEmpty()) {
                    _currentState.value = State.Data(tracksDomainList.map { it.toTrackLibraryUI() })
                } else {
                    _currentState.value = State.NoData
                }
            }
        }
    }

    fun handleItemClick(item: TrackLibraryItem) {
        val track = tracksDomainList.firstOrNull { it.trackId == item.trackId }

        if (track != null) {
            _navigationEvent.value = track!!
        }
    }

    sealed class State {
        class Data(val list: List<TrackLibraryItem>) : State()
        data object NoData : State()
    }
}
