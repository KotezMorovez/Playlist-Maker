package com.example.playlist_maker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks_table")
data class TrackDbEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String?,
    val previewUrl: String?
)