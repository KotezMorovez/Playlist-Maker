package com.example.playlist_maker.data.database.track

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks")
data class TrackDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "track_id")
    val trackId: String,
    @ColumnInfo(name = "track_name")
    val trackName: String,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "track_time_millis")
    val trackTimeMillis: Int,
    @ColumnInfo(name = "collection_name")
    val collectionName: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "primary_genre_name")
    val primaryGenreName: String,
    val country: String,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl100: String?,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String?
)