package com.example.playlist_maker.domain.itunes_api.dto

import com.example.playlist_maker.domain.prefs.dto.Track

data class TrackList (
    val resultCount: Int,
    val tracks: List<Track>
)