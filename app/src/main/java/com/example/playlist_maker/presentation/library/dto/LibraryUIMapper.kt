package com.example.playlist_maker.presentation.library.dto

import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.prefs.dto.Track
import java.text.SimpleDateFormat
import java.util.Locale

fun Track.toTrackLibraryUI(): TrackLibraryItem {
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

fun Playlist.toPlaylistLibraryUI(): PlaylistLibraryItem {
    return PlaylistLibraryItem(
        id = this.id,
        imageUri = this.imageUri,
        name = this.name,
        trackCount = this.tracksCount
    )
}