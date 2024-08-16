package com.example.playlist_maker.domain.interactors

import com.example.playlist_maker.domain.model.TrackList
import com.example.playlist_maker.domain.model.request.SearchRequest
import com.example.playlist_maker.domain.repository_api.SearchRepository

interface SearchUseCase {
    suspend fun getTracks(searchRequest: SearchRequest): Result<TrackList>
}

class SearchUseCaseImpl(
    private val searchRepository: SearchRepository
) : SearchUseCase {
    override suspend fun getTracks(searchRequest: SearchRequest): Result<TrackList> {
        return searchRepository.getTracks(searchRequest)
    }
}