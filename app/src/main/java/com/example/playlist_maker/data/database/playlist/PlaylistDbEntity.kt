package com.example.playlist_maker.data.database.playlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistDbEntity(
    @PrimaryKey
    val id: String,
    val imageUri: String,
    val name: String,
    val description: String,
    val tracksIdList: List<String>
)
