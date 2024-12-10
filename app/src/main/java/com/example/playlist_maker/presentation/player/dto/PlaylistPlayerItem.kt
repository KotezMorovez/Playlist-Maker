package com.example.playlist_maker.presentation.player.dto

data class PlaylistPlayerItem(
    val id: String,
    val coverImageURI: String,
    val name: String,
    val tracksCount: Int
)
