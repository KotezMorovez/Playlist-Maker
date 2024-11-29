package com.example.playlist_maker.data.library

import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.track.TrackDao
import com.example.playlist_maker.data.database.track.toDomain
import com.example.playlist_maker.domain.library.repository_api.LibraryRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LibraryRepositoryImpl(
    private val trackDao: TrackDao,
    private val playlistDao: PlaylistDao
) : LibraryRepository {
    override suspend fun getTracksSubscribtion(): Flow<List<Track>> {
        return withContext(Dispatchers.IO) {
            trackDao.getAllTracks().map {
                it.map { track ->
                    track.toDomain()
                }
            }
        }
    }

    override suspend fun savePlaylist(playlist: PlaylistEntity): Result<Unit> {
        //TODO("Not yet implemented")
        playlistDao.addPlaylist(playlist = playlist.toDbEntity)
    }
}