package com.example.playlist_maker.presentation.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackItem(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String?,
    val previewUrl: String
): Parcelable