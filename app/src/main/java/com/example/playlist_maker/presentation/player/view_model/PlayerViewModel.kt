package com.example.playlist_maker.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.player.interactor.PlayerInteractor
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.player.dto.PlayerStateUI
import com.example.playlist_maker.presentation.player.dto.PlaylistPlayerItem
import com.example.playlist_maker.presentation.player.dto.TrackUI
import com.example.playlist_maker.presentation.player.dto.toDomain
import com.example.playlist_maker.presentation.player.dto.toPlayerUI
import com.example.playlist_maker.presentation.player.dto.toPlaylistPlayerUI
import com.example.playlist_maker.utils.SingleLiveEvent
import com.example.playlist_maker.utils.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    private var timer: Timer? = null
    private var trackDomain: Track? = null
    private var _currentState: MutableLiveData<State> = MutableLiveData(State.default())
    val currentState: LiveData<State>
        get() = _currentState

    private var _playlistEvent: SingleLiveEvent<Triple<Boolean, String, Long>> = SingleLiveEvent()
    val playlistEvent = _playlistEvent

    fun initialize(item: Track) {
        trackDomain = item
        _currentState.value = _currentState.value?.copy(track = trackDomain?.toPlayerUI())

        if (!checkPlayer()) {
            val uri = _currentState.value?.track?.previewUrl
            if (uri != null) {
                preparePlayer(uri)
            }
        }

        if (timer == null) {
            initTimer()
        }

        if (trackDomain != null) {
            viewModelScope.launch {
                val isFavourite = playerInteractor.isTrackInFavourite(trackDomain!!.trackId)
                _currentState.value = _currentState.value?.copy(isFavourite = isFavourite)
            }
        }
    }

    fun applyPlayerScreenState() {
        val isReady = checkPlayer()
        if (isReady) {
            val state = _currentState.value ?: return
            val isPlay = !state.isPlay
            _currentState.value = _currentState.value?.copy(isPlay = isPlay)

            applyPlayerState(isPlay)
            toggleTimer(isPlay)
        }
    }

    fun resetPlayerScreen(isFinished: Boolean) {
        if (isFinished) {
            releasePlayer()
            resetTimer()
        }
    }

    fun showPlaylists() {
        viewModelScope.launch {
            loadPlaylists().collect {
                _currentState.value = _currentState.value?.copy(playlists = it)
            }
        }
    }

    fun toggleMedia(event: Boolean) {
        _currentState.value = _currentState.value?.copy(isInMedia = event)
    }

    fun toggleFavourite() {
        val state = _currentState.value ?: return
        if (trackDomain != null) {
            updateDatabase(state)
            val isFavourite = !state.isFavourite
            _currentState.value = _currentState.value?.copy(isFavourite = isFavourite)
        }
    }

    fun stopPlayerScreen() {
        if (_currentState.value?.isPlay == true) {
            _currentState.value = _currentState.value?.copy(isPlay = false)
            timer?.pause()
            playerInteractor.applyState(PlayerStateUI.PAUSED.toDomain())
        }
    }

    fun handleItemClick(item: PlaylistPlayerItem) {
        if (trackDomain != null) {
            viewModelScope.launch {
                _playlistEvent.value =
                    Triple(
                        playerInteractor.tryToAddTrackToPlaylist(
                            item.id,
                            trackDomain!!
                        ),
                        item.name,
                        System.currentTimeMillis()
                    )
            }
        }
    }

    private suspend fun loadPlaylists(): Flow<List<PlaylistPlayerItem>> {
        return playerInteractor.loadPlaylists().map { list -> list.map { it.toPlaylistPlayerUI() } }
    }

    private fun updateDatabase(state: State) {
        viewModelScope.launch {
            if (state.isFavourite) {
                playerInteractor.deleteTrackFromFavourite(trackDomain!!)
            } else {
                playerInteractor.addTrackToFavourite(
                    trackDomain!!.copy(timestampFavourite = System.currentTimeMillis(), isFavourite = true)
                )
            }
        }
    }

    private fun checkPlayer(): Boolean {
        return playerInteractor.isStatePrepared()
    }

    private fun preparePlayer(uri: String) {
        viewModelScope.launch {
            playerInteractor.preparePlayer(uri)
        }
    }

    private fun applyPlayerState(isPlay: Boolean) {
        playerInteractor.applyState(
            if (isPlay)
                PlayerStateUI.STARTED.toDomain()
            else
                PlayerStateUI.PAUSED.toDomain()
        )
    }

    private fun releasePlayer() {
        playerInteractor.applyState(PlayerStateUI.RELEASED.toDomain())
    }

    private fun initTimer() {
        timer = Timer(TIMER_DELAY, PREVIEW_TIME, viewModelScope) { timeLeft ->
            val timeLeftSeconds = Math.round(timeLeft / 1000f).toLong()
            _currentState.value = _currentState.value?.copy(
                timeLeft = String.format("%02d:%02d", timeLeftSeconds / 60, timeLeftSeconds)
            )

            if (timer?.state == Timer.State.IDLE) {
                _currentState.value = _currentState.value?.copy(
                    timeLeft = END_OF_PREVIEW,
                    isPlay = false
                )
            }
        }
    }

    private fun toggleTimer(isPlay: Boolean) {
        if (isPlay) {
            timer?.run()
        } else {
            timer?.pause()
        }
    }

    private fun resetTimer() {
        timer?.reset()
    }

    companion object {
        private const val PREVIEW_TIME_TEXT = "00:30"
        private const val END_OF_PREVIEW = "00:00"
        private const val PREVIEW_TIME = 30000L
        private const val TIMER_DELAY = 1000L
    }

    data class State(
        var isPlay: Boolean = false,
        var isFavourite: Boolean = false,
        var isInMedia: Boolean = false,
        var timeLeft: String,
        var track: TrackUI? = null,
        var playlists: List<PlaylistPlayerItem> = listOf()
    ) {
        companion object {
            fun default(): State {
                return State(timeLeft = PREVIEW_TIME_TEXT)
            }
        }
    }
}