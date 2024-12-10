package com.example.playlist_maker.data.database.playlist_to_track

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_to_track")
data class PlaylistToTrackDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "playlist_id")
    val playlistId: String,
    @ColumnInfo(name = "track_id")
    val trackId: String,
    val timestamp: Long
)