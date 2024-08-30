package com.example.playlist_maker.presentation.mapper

import com.example.playlist_maker.domain.model.PlayerState
import com.example.playlist_maker.presentation.models.PlayerStateUI

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