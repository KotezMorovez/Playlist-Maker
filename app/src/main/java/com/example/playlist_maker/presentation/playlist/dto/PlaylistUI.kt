package com.example.playlist_maker.presentation.playlist.dto

data class PlaylistUI(
    val imageUri: String,
    val name: String,
    val description: String,
    val tracksCount: Int,
    val totalDuration: Int,
)
