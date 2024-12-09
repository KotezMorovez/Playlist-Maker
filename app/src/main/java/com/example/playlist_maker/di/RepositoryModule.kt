package com.example.playlist_maker.di

import com.example.playlist_maker.data.itunes_api.repository.SearchRepositoryImpl
import com.example.playlist_maker.data.player.repository.PlayerRepositoryImpl
import com.example.playlist_maker.data.playlist.PlaylistRepositoryImpl
import com.example.playlist_maker.data.storage.repository.ImageStorageRepositoryImpl
import com.example.playlist_maker.data.storage.repository.PrefsRepositoryImpl
import com.example.playlist_maker.data.track.repository.TrackRepositoryImpl
import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository
import com.example.playlist_maker.domain.library.repository_api.PlaylistRepository
import com.example.playlist_maker.domain.library.repository_api.TrackRepository
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.repository_api.ImageStorageRepository
import com.example.playlist_maker.domain.prefs.repository_api.PrefsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<PrefsRepository> {
        PrefsRepositoryImpl(get())
    }

    factory<ImageStorageRepository> {
        ImageStorageRepositoryImpl(get())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(get())
    }
}