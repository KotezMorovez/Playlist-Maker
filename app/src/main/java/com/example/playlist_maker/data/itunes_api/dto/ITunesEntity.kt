package com.example.playlist_maker.data.itunes_api.dto

import com.example.playlist_maker.data.prefs.dto.TrackEntity

// FixMe: класс был создан и размещен до рефакторинга
data class ITunesEntity(
    val resultCount: Int,
    val results: List<TrackEntity>
)