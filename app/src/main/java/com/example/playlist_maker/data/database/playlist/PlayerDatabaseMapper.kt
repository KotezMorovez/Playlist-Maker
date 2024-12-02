package com.example.playlist_maker.data.database.playlist

import com.example.playlist_maker.domain.library.dto.Playlist

fun Playlist.toDbEntity(): PlaylistDbEntity {
    return PlaylistDbEntity(
        id = this.id,
        name = this.name,
        imageUri = this.imageUri,
        description = this.description
    )
}