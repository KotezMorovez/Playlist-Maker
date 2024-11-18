package com.example.playlist_maker.presentation.library.dto

data class TrackLibraryItem(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String?,
)