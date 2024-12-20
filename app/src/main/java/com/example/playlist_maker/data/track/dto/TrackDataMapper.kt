package com.example.playlist_maker.data.track.dto

import com.example.playlist_maker.data.itunes_api.dto.ITunesEntity
import com.example.playlist_maker.domain.itunes_api.dto.TrackList
import com.example.playlist_maker.domain.prefs.dto.Track

fun ITunesEntity.toDomain(): TrackList {
    return TrackList(
        resultCount = this.resultCount,
        tracks = this.results.mapNotNull {
            it.toDomain()
        }
    )
}

fun TrackEntity.toDomain(): Track? {
    if (previewUrl == null) {
        return null
    }

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
        previewUrl = this.previewUrl
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
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
