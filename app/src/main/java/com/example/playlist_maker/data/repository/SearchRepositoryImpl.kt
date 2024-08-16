package com.example.playlist_maker.data.repository

import com.example.playlist_maker.data.mapper.toDomain
import com.example.playlist_maker.data.service.SearchService
import com.example.playlist_maker.domain.model.TrackList
import com.example.playlist_maker.domain.model.request.SearchRequest
import com.example.playlist_maker.domain.repository_api.SearchRepository

class SearchRepositoryImpl(
    private val searchService: SearchService
) : SearchRepository {


    override suspend fun getTracks(searchRequest: SearchRequest): Result<TrackList> {
        return searchService.getSearch(searchRequest).map { it.toDomain() }
    }
}