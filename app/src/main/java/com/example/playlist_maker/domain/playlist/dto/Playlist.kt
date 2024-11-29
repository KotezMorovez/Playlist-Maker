package com.example.playlist_maker.domain.playlist.dto

import com.example.playlist_maker.domain.prefs.dto.Track

data class Playlist(
    val id: String,
    val image: String,
    val name: String,
    val description: String,
    val tracks: List<Track>
)
