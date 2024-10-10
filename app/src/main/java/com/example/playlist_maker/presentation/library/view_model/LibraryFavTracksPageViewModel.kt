package com.example.playlist_maker.presentation.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.presentation.search.dto.TrackItem

class LibraryFavTracksPageViewModel : ViewModel() {
    private var _currentState: MutableLiveData<State> = MutableLiveData(State.NoData)
    val currentState: LiveData<State>
        get() = _currentState


    sealed class State {
        class Data(val list: List<TrackItem>) : State()
        data object NoData : State()
        data object Error : State()
    }
}
