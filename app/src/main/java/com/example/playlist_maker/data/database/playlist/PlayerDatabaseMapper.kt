package com.example.playlist_maker.data.database.playlist

import com.example.playlist_maker.domain.library.dto.Playlist

fun Playlist.toDbEntity(): PlaylistDbEntity {
    return PlaylistDbEntity(
        id = this.id,
        name = this.name,
        imageUri = this.imageUri,
        description = this.description,
        timestamp = this.timestamp
    )
}

fun playlistDbEntityToDomain(entity: PlaylistDbEntity, count: Int): Playlist {
    return Playlist(
        id = entity.id,
        name = entity.name,
        imageUri = entity.imageUri,
        description = entity.description,
        tracksCount = count,
        timestamp = entity.timestamp
    )
}