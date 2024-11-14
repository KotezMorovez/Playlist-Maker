package com.example.playlist_maker.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.domain.player.interactor.PlayerInteractor
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.player.dto.PlayerStateUI
import com.example.playlist_maker.presentation.player.dto.TrackUI
import com.example.playlist_maker.presentation.player.dto.toDomain
import com.example.playlist_maker.presentation.player.dto.toPlayerUI
import com.example.playlist_maker.utils.Timer
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    private var timer: Timer? = null

    private var _currentState: MutableLiveData<State> = MutableLiveData(State.default())
    val currentState: LiveData<State>
        get() = _currentState

    fun initialize(item: Track) {
        _currentState.value = _currentState.value?.copy(track = item.toPlayerUI())

        if (!checkPlayer()) {
            val uri = _currentState.value?.track?.previewUrl
            if (uri != null) {
                preparePlayer(uri)
            }
        }

        if (timer == null) {
            initTimer()
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

    fun toggleMedia() {
        val state = _currentState.value ?: return
        val isInMedia = !state.isInMedia
        _currentState.value = _currentState.value?.copy(isInMedia = isInMedia)
    }

    fun toggleFavourite() {
        val state = _currentState.value ?: return
        val isFavourite = !state.isFavourite
        _currentState.value = _currentState.value?.copy(isFavourite = isFavourite)
    }

    fun stopPlayerScreen() {
        if (_currentState.value?.isPlay == true) {
            _currentState.value = _currentState.value?.copy(isPlay = false)
            timer?.pause()
            playerInteractor.applyState(PlayerStateUI.PAUSED.toDomain())
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
        var track: TrackUI? = null
    ) {
        companion object {
            fun default(): State {
                return State(timeLeft = PREVIEW_TIME_TEXT)
            }
        }
    }
}