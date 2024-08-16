package com.example.playlist_maker.presentation.mapper

import com.example.playlist_maker.domain.model.Track
import com.example.playlist_maker.presentation.models.TrackItem
import java.text.SimpleDateFormat
import java.util.Locale

fun Track.toUI(): TrackItem {
    val releaseDate = if (this.releaseDate != null) {
        val date = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.getDefault()
        ).parse(this.releaseDate)

        if (date != null) {
            SimpleDateFormat(
                "yyyy",
                Locale.getDefault()
            ).format(date)
        } else {
            "no data"
        }
    } else {
        "no data"
    }

    return TrackItem(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(this.trackTimeMillis),
        collectionName = this.collectionName,
        releaseDate = releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        artworkUrl100 = this.artworkUrl100,
        previewUrl = this.previewUrl
    )
}