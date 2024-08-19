package com.example.playlist_maker.domain.repository_api

import com.example.playlist_maker.domain.model.TrackList

interface SearchRepository {
    suspend fun getTracks(searchRequest: String): Result<TrackList>
}