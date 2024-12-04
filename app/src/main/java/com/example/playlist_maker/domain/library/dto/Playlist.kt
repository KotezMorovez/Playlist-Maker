package com.example.playlist_maker.domain.library.dto

data class Playlist(
    val id: String,
    val imageUri: String,
    val name: String,
    val description: String,
    val tracksCount: Int
)
