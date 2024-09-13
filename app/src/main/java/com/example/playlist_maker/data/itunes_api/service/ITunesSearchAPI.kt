package com.example.playlist_maker.data.itunes_api.service

import com.example.playlist_maker.data.itunes_api.dto.ITunesEntity
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ITunesSearchAPI {
    @GET("/search?entity=song")
    suspend fun getSearch(
        @Query("term") text: String
    ): Response<ITunesEntity>
}