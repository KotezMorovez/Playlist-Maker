package com.example.playlist_maker.presentation.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractor

class SettingsViewModel(
    private val themeInteractor: ThemeInteractor
): ViewModel() {
    private var _themeState: MutableLiveData<Boolean> = MutableLiveData(false)
    val themeState: LiveData<Boolean>
        get() = _themeState

    fun saveCurrentTheme(checked: Boolean) {
        themeInteractor.saveTheme(checked)
    }

    fun isDarkTheme() {
        _themeState.value = themeInteractor.getTheme()
    }
}