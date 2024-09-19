package com.example.playlist_maker.di

import com.example.playlist_maker.domain.itunes_api.interactor.SearchUseCase
import com.example.playlist_maker.domain.itunes_api.interactor.SearchUseCaseImpl
import com.example.playlist_maker.domain.player.interactor.PlayerInteractor
import com.example.playlist_maker.domain.player.interactor.PlayerInteractorImpl
import com.example.playlist_maker.domain.prefs.interactor.HistoryInteractor
import com.example.playlist_maker.domain.prefs.interactor.HistoryInteractorImpl
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractor
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    factory<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    factory<SearchUseCase> {
        SearchUseCaseImpl(get())
    }
}