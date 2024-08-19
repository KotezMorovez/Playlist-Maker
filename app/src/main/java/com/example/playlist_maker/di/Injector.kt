package com.example.playlist_maker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlist_maker.data.repository.PlayerRepositoryImpl
import com.example.playlist_maker.data.repository.PrefsRepositoryImpl
import com.example.playlist_maker.data.repository.SearchRepositoryImpl
import com.example.playlist_maker.data.service.ITunesSearchAPI
import com.example.playlist_maker.data.service.PlayerService
import com.example.playlist_maker.data.service.PlayerServiceImpl
import com.example.playlist_maker.data.service.PrefsStorage
import com.example.playlist_maker.data.service.PrefsStorageImpl
import com.example.playlist_maker.data.service.SearchService
import com.example.playlist_maker.data.service.SearchServiceImpl
import com.example.playlist_maker.domain.interactors.HistoryInteractor
import com.example.playlist_maker.domain.interactors.HistoryInteractorImpl
import com.example.playlist_maker.domain.interactors.PlayerInteractor
import com.example.playlist_maker.domain.interactors.PlayerInteractorImpl
import com.example.playlist_maker.domain.interactors.SearchUseCase
import com.example.playlist_maker.domain.interactors.SearchUseCaseImpl
import com.example.playlist_maker.domain.interactors.ThemeInteractor
import com.example.playlist_maker.domain.interactors.ThemeInteractorImpl
import com.example.playlist_maker.domain.repository_api.PlayerRepository
import com.example.playlist_maker.domain.repository_api.PrefsRepository
import com.example.playlist_maker.domain.repository_api.SearchRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injector {
    private lateinit var sharedPreferences: SharedPreferences
    private var searchApi: ITunesSearchAPI? = null
    private const val PREFERENCES = "playlist_maker_shared_preferences"
    private const val BASE_URL = "https://itunes.apple.com"

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

    fun getSearchUseCase(): SearchUseCase {
        return SearchUseCaseImpl(getSearchRepository())
    }

    fun getHistoryInteractor(): HistoryInteractor {
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
}