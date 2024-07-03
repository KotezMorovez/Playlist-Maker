package com.example.playlist_maker.data.service

import com.example.playlist_maker.data.model.ITunesEntity
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response

interface ITunesSearchAPI {
    @GET("/search?entity=song")
    suspend fun getSearch(
        @Query("term") text: String
    ): Response<ITunesEntity>

    companion object {
        private var instance: ITunesSearchAPI? = null
        private val interceptor = HttpLoggingInterceptor()

        fun getInstance(): ITunesSearchAPI {
            if (instance == null) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                instance = Retrofit.Builder()
                    .baseUrl("https://itunes.apple.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .build()
                    .create(ITunesSearchAPI::class.java)
            }
            return instance!!
        }
    }
}