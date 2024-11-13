package com.example.playlist_maker.domain.itunes_api.interactor

import com.example.playlist_maker.domain.itunes_api.dto.TrackList
import com.example.playlist_maker.domain.itunes_api.repository_api.SearchRepository
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    fun getTracks(searchRequest: String): Flow<TrackList>
}

class SearchUseCaseImpl(
    private val searchRepository: SearchRepository
) : SearchUseCase {
    override fun getTracks(searchRequest: String): Flow<TrackList> {
        return searchRepository.getTracks(searchRequest)
    }
}