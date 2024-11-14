package com.example.playlist_maker.data.itunes_api.repository

import com.example.playlist_maker.data.itunes_api.service.SearchService
import com.example.playlist_maker.data.prefs.dto.toDomain
import com.example.playlist_maker.domain.itunes_api.dto.TrackList
import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(
    private val searchService: SearchService
) : SearchRepository {

    override fun getTracks(searchRequest: String): Flow<TrackList> {
        return searchService.getSearch(searchRequest).map { it.toDomain() }
    }
}