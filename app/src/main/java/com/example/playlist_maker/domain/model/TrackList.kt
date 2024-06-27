package com.example.playlist_maker.domain.model

import com.example.playlist_maker.data.model.TrackEntity

data class TrackList (
    val resultCount: Int,
    val tracks: List<Track>
)