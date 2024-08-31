package com.example.playlist_maker.presentation.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractor

class SettingsViewModel(
    private val themeInteractor: ThemeInteractor
): ViewModel() {

    fun saveCurrentTheme(checked: Boolean) {
        themeInteractor.saveTheme(checked)
    }
}