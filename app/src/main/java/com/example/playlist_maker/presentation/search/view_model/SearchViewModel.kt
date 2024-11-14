package com.example.playlist_maker.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.itunes_api.interactor.SearchUseCase
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.domain.prefs.interactor.HistoryInteractor
import com.example.playlist_maker.presentation.search.dto.TrackItem
import com.example.playlist_maker.presentation.search.dto.toUI
import com.example.playlist_maker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyInteractor: HistoryInteractor,
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private var _currentState: MutableLiveData<State> = MutableLiveData(State.History(listOf()))
    val currentState: LiveData<State>
        get() = _currentState

    private val _navigationEvent = SingleLiveEvent<Track>()
    val navigationEvent: LiveData<Track>
        get() = _navigationEvent

    private var historyDomainList: List<Track> = listOf()
    private var searchResultDomainList: List<Track> = listOf()
    private var loadTracksJob: Job? = null

    var lastSearchRequest = EMPTY_STRING
        private set

    fun init() {
        val state = _currentState.value

        if (state is State.History) {
            historyDomainList = historyInteractor.getHistory()
            _currentState.value = State.History(historyDomainList.map { it.toUI() })
        } else if (state is State.Data && searchResultDomainList.isNotEmpty()) {
            _currentState.value = State.Data(searchResultDomainList.map { it.toUI() })
        }
    }

    fun clearSearchResult() {
        searchResultDomainList = listOf()
    }

    fun handleItemClick(item: TrackItem, isHistoryList: Boolean) {
        val track = getTrack(item, isHistoryList)

        if (track != null) {
            historyDomainList = historyInteractor.updateHistory(track)

            if (isHistoryList) {
                _currentState.value = State.History(historyDomainList.map { it.toUI() })
            }

            _navigationEvent.value = track!!
        }
    }

    fun setSearchRequest(request: String) {
        lastSearchRequest = request
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    fun actualizeState(isFieldHasFocus: Boolean) {
        _currentState.value = if (
            lastSearchRequest.isEmpty()
            && isFieldHasFocus
            && !historyInteractor.isHistoryEmpty()
        ) {
            State.History(historyDomainList.map { it.toUI() })
        } else if (lastSearchRequest.isNotEmpty()) {
            State.Loading
        } else {
            State.Data(searchResultDomainList.map { it.toUI() })
        }
    }

    fun loadTracks(text: String) {
        loadTracksJob?.cancel()

        loadTracksJob = viewModelScope.launch {
            if (text.isNotBlank()) {
                searchUseCase.getTracks(text)
                    .catch {
                        _currentState.value = State.Error
                    }
                    .collect { trackList ->
                        if (trackList.resultCount != 0) {
                            searchResultDomainList = trackList.tracks
                            _currentState.value =
                                State.Data(searchResultDomainList.map { it.toUI() })

                        } else {
                            _currentState.value = State.NoData
                        }
                    }
            } else {
                _currentState.value = State.Error
            }
        }
    }

    private fun getTrack(item: TrackItem, isHistoryList: Boolean): Track? {
        return if (isHistoryList) {
            historyDomainList.firstOrNull { it.trackId == item.trackId }
        } else {
            searchResultDomainList.firstOrNull { it.trackId == item.trackId }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

    sealed class State {
        class Data(val list: List<TrackItem>) : State()
        data object NoData : State()
        data object Error : State()
        class History(val list: List<TrackItem>) : State()
        data object Loading : State()
    }
}