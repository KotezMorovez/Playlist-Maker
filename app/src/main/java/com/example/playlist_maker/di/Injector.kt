package com.example.playlist_maker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlist_maker.data.player.repository.PlayerRepositoryImpl
import com.example.playlist_maker.data.prefs.repository.PrefsRepositoryImpl
import com.example.playlist_maker.data.itunes_api.repository.SearchRepositoryImpl
import com.example.playlist_maker.data.itunes_api.service.ITunesSearchAPI
import com.example.playlist_maker.data.player.service.PlayerService
import com.example.playlist_maker.data.player.service.PlayerServiceImpl
import com.example.playlist_maker.data.prefs.service.PrefsStorage
import com.example.playlist_maker.data.prefs.service.PrefsStorageImpl
import com.example.playlist_maker.data.itunes_api.service.SearchService
import com.example.playlist_maker.data.itunes_api.service.SearchServiceImpl
import com.example.playlist_maker.domain.prefs.interactor.HistoryInteractor
import com.example.playlist_maker.domain.prefs.interactor.HistoryInteractorImpl
import com.example.playlist_maker.domain.player.interactor.PlayerInteractor
import com.example.playlist_maker.domain.player.interactor.PlayerInteractorImpl
import com.example.playlist_maker.domain.itunes_api.interactor.SearchUseCase
import com.example.playlist_maker.domain.itunes_api.interactor.SearchUseCaseImpl
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractor
import com.example.playlist_maker.domain.prefs.interactor.ThemeInteractorImpl
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.repository_api.PrefsRepository
import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injector {
    private lateinit var sharedPreferences: SharedPreferences
    private var searchApi: ITunesSearchAPI? = null
    private const val PREFERENCES = "playlist_maker_shared_preferences"
    private const val BASE_URL = "https://itunes.apple.com"

    fun getViewModelFactory(): ViewModelFactory {
        return ViewModelFactory(
            historyInteractor = getHistoryInteractor(),
            themeInteractor = getThemeInteractor(),
            playerInteractor = getPlayerInteractor(),
            searchUseCase = getSearchUseCase()
        )
    }

    private fun getSearchUseCase(): SearchUseCase {
        return SearchUseCaseImpl(getSearchRepository())
    }

    private fun getHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getPrefsRepository())
    }

    fun getThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getPrefsRepository())
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun initializeDependencyWithContext(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }

    private fun getSearchApi(): ITunesSearchAPI {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        if (searchApi == null) {
            searchApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                .build()
                .create(ITunesSearchAPI::class.java)
        }
        return searchApi!!
    }

    private fun getSharedPrefs(): SharedPreferences {
        return sharedPreferences
    }

    private fun getSearchService(): SearchService {
        return SearchServiceImpl(getSearchApi())
    }

    private fun getPrefsStorage(): PrefsStorage {
        return PrefsStorageImpl(getSharedPrefs())
    }

    private fun getPlayerService(): PlayerService {
        return PlayerServiceImpl()
    }

    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(getSearchService())
    }

    private fun getPrefsRepository(): PrefsRepository {
        return PrefsRepositoryImpl(getPrefsStorage())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(getPlayerService())
    }
}