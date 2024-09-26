package com.example.playlist_maker.data.prefs.repository


import com.example.playlist_maker.data.prefs.dto.toDomain
import com.example.playlist_maker.data.prefs.dto.toEntity
import com.example.playlist_maker.data.prefs.service.PrefsStorage
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.domain.prefs.repository_api.PrefsRepository

class PrefsRepositoryImpl(
    private val prefsStorage: PrefsStorage
) : PrefsRepository {
    override fun getHistory(): List<Track> {
        return prefsStorage.getHistory().mapNotNull { it.toDomain() }
    }

    override fun setHistory(list: List<Track>) {
        prefsStorage.setHistory(list.map { it.toEntity() })
    }

    override fun getFirstLaunchFlag(): Boolean {
        return prefsStorage.getFirstLaunchFlag()
    }

    override fun disableFirstLaunchFlag() {
        prefsStorage.disableFirstLaunchFlag()
    }

    override fun setTheme(isDarkTheme: Boolean) {
        prefsStorage.setTheme(isDarkTheme)
    }

    override fun getTheme(): Boolean {
        return prefsStorage.getTheme()
    }

    override fun clearHistory() {
        prefsStorage.clearHistory()
    }

    override fun isHistoryEmpty(): Boolean {
        return prefsStorage.isHistoryEmpty()
    }
}