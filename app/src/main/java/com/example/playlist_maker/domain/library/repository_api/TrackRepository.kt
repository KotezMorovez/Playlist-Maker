package com.example.playlist_maker.domain.library.repository_api

import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun getTracksSubscription(): Flow<List<Track>>
    suspend fun addTrackToFavourite(track: Track)
    suspend fun deleteTrackFromFavourite(track: Track)
    suspend fun isTrackInFavourite(id: String): Boolean
    suspend fun isTrackInPlaylist(playlistId: String, trackId: String): Boolean
    suspend fun addTrackToPlaylist(playlistId: String, track: Track)
}