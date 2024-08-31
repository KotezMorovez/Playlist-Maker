package com.example.playlist_maker.data.player.dto

import com.example.playlist_maker.domain.player.dto.PlayerState

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