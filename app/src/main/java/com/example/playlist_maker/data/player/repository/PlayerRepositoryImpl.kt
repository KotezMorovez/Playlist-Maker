package com.example.playlist_maker.data.player.repository

import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.playlist.playlistDbEntityToDomain
import com.example.playlist_maker.data.database.track.TrackDao
import com.example.playlist_maker.data.database.track.toDatabase
import com.example.playlist_maker.data.player.dto.toEntity
import com.example.playlist_maker.data.player.service.PlayerService
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(
    private val audioPlayerService: PlayerService,
    private val trackDb: TrackDao,
    private val playlistDb: PlaylistDao
) : PlayerRepository {
    override fun preparePlayer(previewUrl: String) {
        audioPlayerService.preparePlayer(previewUrl)
    }

    override fun applyState(state: PlayerState) {
        audioPlayerService.applyState(state.toEntity())
    }

    override fun isStatePrepared(): Boolean {
        return audioPlayerService.isStatePrepared()
    }

    override fun loadPlaylists(): Flow<List<Playlist>> {
        return playlistDb.getAllPlaylists().map { list ->
            list.map {
                playlistDbEntityToDomain(it)
            }
        }
    }

    override suspend fun addTrackToFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            trackDb.addTrack(track.toDatabase())
        }
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            trackDb.deleteTrack(track.toDatabase())
        }
    }

    override suspend fun isTrackInFavourite(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val foundTrack = trackDb.findTrackInTable(id)
            foundTrack != null
        }
    }
}