package com.example.playlist_maker.di

import com.example.playlist_maker.data.itunes_api.repository.SearchRepositoryImpl
import com.example.playlist_maker.data.library.LibraryRepositoryImpl
import com.example.playlist_maker.data.player.repository.PlayerRepositoryImpl
import com.example.playlist_maker.data.prefs.repository.PrefsRepositoryImpl
import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository
import com.example.playlist_maker.domain.library.repository_api.LibraryRepository
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.repository_api.PrefsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<LibraryRepository> {
        LibraryRepositoryImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }

    factory<PrefsRepository> {
        PrefsRepositoryImpl(get())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(get())
    }
}