package com.example.playlist_maker.di

import com.example.playlist_maker.presentation.library.view_model.LibraryFavTracksPageViewModel
import com.example.playlist_maker.presentation.library.view_model.LibraryPlaylistsPageViewModel
import com.example.playlist_maker.presentation.library.view_model.LibraryViewModel
import com.example.playlist_maker.presentation.main.view_model.MainViewModel
import com.example.playlist_maker.presentation.player.view_model.PlayerViewModel
import com.example.playlist_maker.presentation.playlist_create.view_model.CreatePlaylistViewModel
import com.example.playlist_maker.presentation.search.view_model.SearchViewModel
import com.example.playlist_maker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LibraryViewModel()
    }

    viewModel {
        LibraryFavTracksPageViewModel(get())
    }

    viewModel {
        LibraryPlaylistsPageViewModel(get())
    }

    viewModel {
        MainViewModel()
    }

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get(), get())
    }
}