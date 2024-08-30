package com.example.playlist_maker.data.mapper

import com.example.playlist_maker.data.model.PlayerStateEntity
import com.example.playlist_maker.domain.model.PlayerState

fun PlayerState.toEntity(): PlayerStateEntity {
    return when (this) {
        PlayerState.PREPARED -> {
            PlayerStateEntity.PREPARED
        }

        PlayerState.PAUSED -> {
            PlayerStateEntity.PAUSED
        }

        PlayerState.RELEASED -> {
            PlayerStateEntity.RELEASED
        }

        PlayerState.STARTED -> {
            PlayerStateEntity.STARTED
        }
    }
}