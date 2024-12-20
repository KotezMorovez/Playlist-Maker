package com.example.playlist_maker.data.database.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistDbEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "image_uri")
    val imageUri: String,
    val name: String,
    val description: String,
    val timestamp: Long
)

//@Entity(tableName = "track_list")
//data class TrackListDbEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
//    @ColumnInfo(name = "playlist_id")
//    val playlistId: String,
//    @ColumnInfo(name = "track_id")
//    val trackId: String,
//    val timestamp: Long
//)