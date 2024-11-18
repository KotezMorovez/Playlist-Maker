package com.example.playlist_maker.presentation.library.dto

import com.example.playlist_maker.domain.prefs.dto.Track
import java.text.SimpleDateFormat
import java.util.Locale

fun Track.toLibraryUI(): TrackLibraryItem {
    return TrackLibraryItem(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(this.trackTimeMillis),
        artworkUrl100 = this.artworkUrl100
    )
}