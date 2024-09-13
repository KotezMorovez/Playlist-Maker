package com.example.playlist_maker.domain.prefs.interactor

import com.example.playlist_maker.domain.prefs.repository_api.PrefsRepository

interface ThemeInteractor {
    fun getFirstLaunchFlag(): Boolean
    fun disableFirstLaunchFlag()
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme(): Boolean
}

class ThemeInteractorImpl(
    private val prefsRepository: PrefsRepository
): ThemeInteractor {
    override fun getFirstLaunchFlag(): Boolean {
        return prefsRepository.getFirstLaunchFlag()
    }

    override fun disableFirstLaunchFlag() {
        prefsRepository.disableFirstLaunchFlag()
    }

    override fun saveTheme(isDarkTheme: Boolean) {
        prefsRepository.setTheme(isDarkTheme)
    }

    override fun getTheme(): Boolean {
        return prefsRepository.getTheme()
    }
}