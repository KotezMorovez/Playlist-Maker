package com.example.playlist_maker.domain.prefs.interactor

import com.example.playlist_maker.domain.prefs.repository_api.PrefsRepository
import com.example.playlist_maker.domain.prefs.dto.Track

interface HistoryInteractor {
    fun updateHistory(track: Track): List<Track>
    fun getHistory(): List<Track>
    fun clearHistory()
    fun isHistoryEmpty(): Boolean
}

class HistoryInteractorImpl(
    private val prefsRepository: PrefsRepository
): HistoryInteractor {
    override fun updateHistory(track: Track): List<Track> {
        var list = prefsRepository.getHistory()
        list = listOf(track) + list.filter { track.trackId != it.trackId }.take(9)
        prefsRepository.setHistory(list)
        return list
    }

    override fun getHistory(): List<Track> {
        return prefsRepository.getHistory()
    }

    override fun clearHistory() {
        prefsRepository.clearHistory()
    }

    override fun isHistoryEmpty(): Boolean {
        return prefsRepository.isHistoryEmpty()
    }
}