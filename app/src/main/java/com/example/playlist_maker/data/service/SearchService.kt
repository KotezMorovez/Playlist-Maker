package com.example.playlist_maker.data.service

import android.util.Log
import com.example.playlist_maker.data.model.ITunesEntity
import com.example.playlist_maker.domain.model.request.SearchRequest
import java.lang.Exception

interface SearchService {
    suspend fun getSearch (searchRequest: SearchRequest): Result<ITunesEntity>
}

class SearchServiceImpl(
    private val iTunesApi: ITunesSearchAPI
): SearchService {
    override suspend fun getSearch (searchRequest: SearchRequest): Result<ITunesEntity> {
        try {
            val response = iTunesApi.getSearch(text = searchRequest.text)

            if (!response.isSuccessful) {
                Log.e (TAG, response.code().toString())
                return Result.failure(Exception(response.errorBody().toString()))
            }

            val tracks = response.body()

            return if (tracks != null) {
                Result.success(tracks)
            } else {
                Result.failure(Exception("Неизвестная ошибка"))
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }

    companion object{
        private const val TAG: String = "PlaylistMaker"
    }
}