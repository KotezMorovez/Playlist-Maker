package com.example.playlist_maker.domain.library.interactor

import com.example.playlist_maker.domain.library.repository_api.TrackRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TrackInteractor {
    suspend fun getTracks(): Flow<List<Track>>
}

class TrackInteractorImpl(
    private val trackRepository: TrackRepository
): TrackInteractor {
    override suspend fun getTracks(): Flow<List<Track>> {
        return trackRepository.getTracksSubscription().map { list -> list.reversed() }
    }
}