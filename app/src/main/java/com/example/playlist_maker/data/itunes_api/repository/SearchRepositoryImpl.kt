package com.example.playlist_maker.data.itunes_api.repository

import com.example.playlist_maker.data.itunes_api.service.SearchService
import com.example.playlist_maker.data.prefs.dto.toDomain
import com.example.playlist_maker.domain.itunes_api.dto.TrackList
import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository

class SearchRepositoryImpl(
    private val searchService: SearchService
) : SearchRepository {


    override suspend fun getTracks(searchRequest: String): Result<TrackList> {
        return searchService.getSearch(searchRequest).map { it.toDomain() }
    }
}