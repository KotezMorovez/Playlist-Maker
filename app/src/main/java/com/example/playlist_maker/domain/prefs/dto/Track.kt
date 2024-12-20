package com.example.playlist_maker.domain.prefs.dto

import java.io.Serializable

data class Track (
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String?,
    val previewUrl: String,
    val isFavourite: Boolean = false,
    val timestampFavourite: Long = 0L
): Serializable