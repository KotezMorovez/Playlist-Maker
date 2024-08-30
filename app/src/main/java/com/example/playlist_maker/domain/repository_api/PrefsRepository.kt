package com.example.playlist_maker.domain.repository_api

import com.example.playlist_maker.domain.model.Track

interface PrefsRepository {
    fun getHistory(): List<Track>
    fun setHistory(list: List<Track>)
    fun getFirstLaunchFlag(): Boolean
    fun disableFirstLaunchFlag()
    fun setTheme(isDarkTheme: Boolean)
    fun getTheme(): Boolean
    fun clearHistory()
    fun isHistoryEmpty(): Boolean
}