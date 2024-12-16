package com.example.playlist_maker.presentation.playlist.dto

import com.example.playlist_maker.domain.library.dto.Playlist

fun Playlist.toPlaylistUI(): PlaylistUI {
    val millisInMinute = 60 * 1000

    return PlaylistUI(
        name = this.name,
        description = this.description,
        imageUri = this.imageUri,
        tracksCount = this.tracksCount,
        totalDuration = this.totalTime / millisInMinute,
    )
}