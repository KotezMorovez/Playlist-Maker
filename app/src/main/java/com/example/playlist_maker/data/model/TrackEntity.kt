package com.example.playlist_maker.data.model

data class TrackEntity(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)