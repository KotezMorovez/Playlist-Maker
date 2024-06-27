package com.example.playlist_maker.domain.model

data class Track (
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)