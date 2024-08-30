package com.example.playlist_maker.data.repository


import com.example.playlist_maker.data.mapper.toDomain
import com.example.playlist_maker.data.mapper.toEntity
import com.example.playlist_maker.data.service.PrefsStorage
import com.example.playlist_maker.domain.model.Track
import com.example.playlist_maker.domain.repository_api.PrefsRepository

class PrefsRepositoryImpl(
    private val prefsStorage: PrefsStorage
) : PrefsRepository {
    override fun getHistory(): List<Track> {
        return prefsStorage.getHistory().map { it.toDomain() }
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