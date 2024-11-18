package com.example.playlist_maker.domain.player.repository_api

import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.prefs.dto.Track

interface PlayerRepository {
    fun preparePlayer(previewUrl: String)
    fun applyState(state: PlayerState)
    fun isStatePrepared(): Boolean
    suspend fun addTrackToFavourite(track: Track)
    suspend fun deleteTrackFromFavourite(track: Track)
    suspend fun isTrackInFavourite(id: String): Boolean
}