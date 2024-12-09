package com.example.playlist_maker.data.track.repository

import com.example.playlist_maker.data.database.playlist_to_track.PlaylistToTrackDao
import com.example.playlist_maker.data.database.playlist_to_track.PlaylistToTrackDbEntity
import com.example.playlist_maker.data.database.track.TrackDao
import com.example.playlist_maker.data.database.track.toDatabase
import com.example.playlist_maker.data.database.track.toDomain
import com.example.playlist_maker.domain.library.repository_api.TrackRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TrackRepositoryImpl(
    private val trackDao: TrackDao,
    private val playlistToTrackDao: PlaylistToTrackDao,
): TrackRepository {
    override suspend fun getTracksSubscription(): Flow<List<Track>> {
        return withContext(Dispatchers.IO) {
            trackDao.getAllFavouriteTracks().map {
                it.map { track ->
                    track.toDomain()
                }
            }
        }
    }

    override suspend fun addTrackToFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            trackDao.addTrack(track.toDatabase())
        }
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            trackDao.deleteTrack(track.toDatabase())
        }
    }

    override suspend fun isTrackInFavourite(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val foundTrack = trackDao.findTrackInFavourites(id)
            foundTrack != null
        }
    }

    override suspend fun isTrackInPlaylist(playlistId: String, trackId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val foundTrack = playlistToTrackDao.findTrackInPlaylist(playlistId, trackId)
            foundTrack != null
        }
    }

    override suspend fun addTrackToPlaylist(playlistId: String, track: Track) {
        playlistToTrackDao.addTrackToPlaylist(
            PlaylistToTrackDbEntity(
                id = 0,
                playlistId = playlistId,
                trackId = track.trackId,
                timestamp = System.currentTimeMillis()
            )
        )
        trackDao.addTrack(track.toDatabase())
    }
}