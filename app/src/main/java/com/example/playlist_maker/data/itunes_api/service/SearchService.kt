package com.example.playlist_maker.data.itunes_api.service

import android.util.Log
import com.example.playlist_maker.data.itunes_api.dto.ITunesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SearchService {
    fun getSearch(searchRequest: String): Flow<ITunesEntity>
}

class SearchServiceImpl(
    private val iTunesApi: ITunesSearchAPI
) : SearchService {
    override fun getSearch(searchRequest: String): Flow<ITunesEntity> = flow {
        val response = iTunesApi.getSearch(text = searchRequest)

        if (!response.isSuccessful) {
            Log.e (TAG, response.code().toString())
            throw Exception(response.errorBody().toString())
        }

        val tracks = response.body()

         if (tracks != null) {
             emit(tracks)
        } else {
            throw Exception("Неизвестная ошибка")
        }
    }

    companion object{
        private const val TAG: String = "PlaylistMaker"
    }
}