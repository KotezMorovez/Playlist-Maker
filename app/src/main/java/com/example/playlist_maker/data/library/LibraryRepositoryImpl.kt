package com.example.playlist_maker.data.library

import com.example.playlist_maker.data.database.TrackDao
import com.example.playlist_maker.data.database.toDomain
import com.example.playlist_maker.domain.library.repository_api.LibraryRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LibraryRepositoryImpl(
    private val db: TrackDao
) : LibraryRepository {
    override suspend fun getTracksSubscribtion(): Flow<List<Track>> {
        return withContext(Dispatchers.IO) {
            db.getAllTracks().map {
                it.map { track ->
                    track.toDomain()
                }
            }
        }
    }
}