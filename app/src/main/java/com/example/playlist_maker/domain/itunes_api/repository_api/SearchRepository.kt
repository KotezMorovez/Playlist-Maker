package com.example.playlist_maker.domain.itunes_api.repository_api

import com.example.playlist_maker.domain.itunes_api.dto.TrackList
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getTracks(searchRequest: String): Flow<TrackList>
}