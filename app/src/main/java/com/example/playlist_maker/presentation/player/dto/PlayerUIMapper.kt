package com.example.playlist_maker.presentation.player.dto

import com.example.playlist_maker.domain.player.dto.PlayerState
import com.example.playlist_maker.domain.prefs.dto.Track
import java.text.SimpleDateFormat
import java.util.Locale

fun PlayerStateUI.toDomain(): PlayerState {
    return when (this) {
        PlayerStateUI.PREPARED -> {
            PlayerState.PREPARED
        }

        PlayerStateUI.PAUSED -> {
            PlayerState.PAUSED
        }

        PlayerStateUI.RELEASED -> {
            PlayerState.RELEASED
        }

        PlayerStateUI.STARTED -> {
            PlayerState.STARTED
        }
    }
}

fun Track.toPlayerUI(): TrackUI {
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
            null
        }
    } else {
        null
    }

    return TrackUI(
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