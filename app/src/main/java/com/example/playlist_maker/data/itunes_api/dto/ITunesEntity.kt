package com.example.playlist_maker.data.itunes_api.dto

import com.example.playlist_maker.data.track.dto.TrackEntity

data class ITunesEntity(
    val resultCount: Int,
    val results: List<TrackEntity>
)