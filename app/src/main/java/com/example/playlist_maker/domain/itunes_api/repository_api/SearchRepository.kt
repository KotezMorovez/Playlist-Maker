package com.example.playlist_maker.domain.itunes_api.repository_api

import com.example.playlist_maker.domain.itunes_api.dto.TrackList

interface SearchRepository {
    suspend fun getTracks(searchRequest: String): Result<TrackList>
}