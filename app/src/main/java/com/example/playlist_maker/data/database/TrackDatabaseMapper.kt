package com.example.playlist_maker.data.database

import com.example.playlist_maker.domain.prefs.dto.Track

fun Track.toDatabase(): TrackDbEntity {
    return TrackDbEntity(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        artworkUrl100 = this.artworkUrl100,
        previewUrl = this.previewUrl
    )
}

fun TrackDbEntity.toDomain(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        artworkUrl100 = this.artworkUrl100,
        previewUrl = this.previewUrl ?: ""
    )
}