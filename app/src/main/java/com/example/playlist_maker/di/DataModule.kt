package com.example.playlist_maker.di

import android.content.Context
import androidx.room.Room
import com.example.playlist_maker.data.database.AppDatabase
import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.playlist_to_track.PlaylistToTrackDao
import com.example.playlist_maker.data.database.track.TrackDao
import com.example.playlist_maker.data.itunes_api.service.ITunesSearchAPI
import com.example.playlist_maker.data.itunes_api.service.SearchService
import com.example.playlist_maker.data.itunes_api.service.SearchServiceImpl
import com.example.playlist_maker.data.player.service.PlayerService
import com.example.playlist_maker.data.player.service.PlayerServiceImpl
import com.example.playlist_maker.data.storage.service.ImageStorage
import com.example.playlist_maker.data.storage.service.ImageStorageImpl
import com.example.playlist_maker.data.storage.service.PrefsStorage
import com.example.playlist_maker.data.storage.service.PrefsStorageImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    factory<PlayerService> {
        PlayerServiceImpl()
    }

    single {
        androidContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }

    single<PrefsStorage> {
        PrefsStorageImpl(get())
    }

    single<ITunesSearchAPI> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            .build()
            .create(ITunesSearchAPI::class.java)
    }

    single<SearchService> {
        SearchServiceImpl(get())
    }

    single<ImageStorage> {
        ImageStorageImpl(androidContext())
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).build()
    }

    single<TrackDao> {
        val db: AppDatabase = get()
        db.trackDao()
    }

    single<PlaylistDao> {
        val db: AppDatabase = get()
        db.playlistDao()
    }

    single<PlaylistToTrackDao> {
        val db: AppDatabase = get()
        db.playlistToTrackDao()
    }
}

private const val BASE_URL = "https://itunes.apple.com"
private const val PREFERENCES = "playlist_maker_shared_preferences"