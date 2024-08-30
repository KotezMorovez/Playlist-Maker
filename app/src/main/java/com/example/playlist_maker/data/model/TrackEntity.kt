package com.example.playlist_maker.data.model

data class TrackEntity(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String?,
    val previewUrl: String
)