package com.example.playlist_maker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker.domain.prefs.interactor.HistoryInteractor
import com.example.playlist_maker.domain.player.interactor.PlayerInteractor
import com.example.playlist_maker.domain.itunes_api.interactor.SearchUseCase
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractor
import com.example.playlist_maker.presentation.library.view_model.LibraryViewModel
import com.example.playlist_maker.presentation.main.view_model.MainViewModel
import com.example.playlist_maker.presentation.player.view_model.PlayerViewModel
import com.example.playlist_maker.presentation.search.view_model.SearchViewModel
import com.example.playlist_maker.presentation.settings.view_model.SettingsViewModel

class ViewModelFactory(
    private val historyInteractor: HistoryInteractor,
    private val themeInteractor: ThemeInteractor,
    private val playerInteractor: PlayerInteractor,
    private val searchUseCase: SearchUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel() as T
            }

            PlayerViewModel::class.java -> {
                PlayerViewModel(playerInteractor) as T
            }

            SearchViewModel::class.java -> {
                SearchViewModel(historyInteractor, searchUseCase) as T
            }

            SettingsViewModel::class.java -> {
                SettingsViewModel(themeInteractor) as T
            }

            LibraryViewModel::class.java -> {
                LibraryViewModel() as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }
}