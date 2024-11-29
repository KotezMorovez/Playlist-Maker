package com.example.playlist_maker.data.database.playlist

import com.example.playlist_maker.domain.playlist.dto.Playlist

fun Playlist.toDatabase(): PlaylistDbEntity {
    return PlaylistDbEntity(
        id = this.id,
        name = this.name,
        imageUri = this.image,
        description = this.description,
        tracksIdList = this.tracks // List<String>
    )
}