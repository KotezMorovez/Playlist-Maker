package com.example.playlist_maker.domain.itunes_api.interactor

import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository
import com.example.playlist_maker.domain.itunes_api.dto.TrackList

interface SearchUseCase {
    suspend fun getTracks(searchRequest: String): Result<TrackList>
}

class SearchUseCaseImpl(
    private val searchRepository: SearchRepository
) : SearchUseCase {
    override suspend fun getTracks(searchRequest: String): Result<TrackList> {
        return searchRepository.getTracks(searchRequest)
    }
}