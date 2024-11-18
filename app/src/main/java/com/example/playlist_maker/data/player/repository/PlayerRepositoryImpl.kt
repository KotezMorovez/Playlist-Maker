package com.example.playlist_maker.data.player.repository

import com.example.playlist_maker.data.database.TrackDao
import com.example.playlist_maker.data.database.toDatabase
import com.example.playlist_maker.data.player.dto.toEntity
import com.example.playlist_maker.data.player.service.PlayerService
import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.player.repository_api.PlayerRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(
    private val audioPlayerService: PlayerService,
    private val database: TrackDao
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

    override suspend fun addTrackToFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            database.addTrack(track.toDatabase())
        }
    }

    override suspend fun deleteTrackFromFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            database.deleteTrack(track.toDatabase())
        }
    }

    override suspend fun isTrackInFavourite(id: String): Boolean {
        var result = false
        withContext(Dispatchers.IO) {
            val findTrackList = database.findTrackInTable(id)
            if (findTrackList.isNotEmpty()) {
                result = true
            }
        }
        return result
    }
}