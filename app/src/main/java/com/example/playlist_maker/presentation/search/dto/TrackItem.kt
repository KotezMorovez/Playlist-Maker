package com.example.playlist_maker.presentation.search.dto

data class TrackItem(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String?,
)