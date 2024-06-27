package com.example.playlist_maker.data.repository

import com.example.playlist_maker.data.mapper.toDomain
import com.example.playlist_maker.data.service.SearchService
import com.example.playlist_maker.data.service.SearchServiceImpl
import com.example.playlist_maker.domain.model.TrackList
import com.example.playlist_maker.domain.model.request.SearchRequest
import com.example.playlist_maker.domain.repository.SearchRepository

// Временное решение до момента введения DI
class SearchRepositoryImpl: SearchRepository {
    private val searchService: SearchService = SearchServiceImpl()

    override suspend fun getTracks(searchRequest: SearchRequest): Result<TrackList> {
        return searchService.getSearch(searchRequest).map { it.toDomain() }
    }

    companion object {
        private var instance: SearchRepository? = null

        fun getInstance(): SearchRepository {
             if (instance == null) {
                instance = SearchRepositoryImpl()
            }
            return instance!!
        }
    }
}