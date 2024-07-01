package com.example.playlist_maker.domain.repository

import com.example.playlist_maker.domain.model.TrackList
import com.example.playlist_maker.domain.model.request.SearchRequest

interface SearchRepository {
    suspend fun getTracks(searchRequest: SearchRequest): Result<TrackList>
}