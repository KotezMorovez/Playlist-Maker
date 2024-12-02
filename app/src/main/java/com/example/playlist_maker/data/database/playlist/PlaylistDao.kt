package com.example.playlist_maker.data.database.playlist

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface PlaylistDao {
    @Insert(entity = PlaylistDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistDbEntity)

    @Insert(entity = TrackListDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackIntoPlaylist(playlistId: String, trackId: String, timestamp: Long)

    @
}