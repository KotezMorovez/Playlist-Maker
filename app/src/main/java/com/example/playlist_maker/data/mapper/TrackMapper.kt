package com.example.playlist_maker.data.mapper

import com.example.playlist_maker.data.model.ITunesEntity
import com.example.playlist_maker.data.model.TrackEntity
import com.example.playlist_maker.domain.model.Track
import com.example.playlist_maker.domain.model.TrackList

fun ITunesEntity.toDomain(): TrackList {
    return TrackList(
        resultCount = this.resultCount,
        tracks = this.results.map {
            it.toDomain()
        }
    )
}

fun TrackEntity.toDomain(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100
    )
}
