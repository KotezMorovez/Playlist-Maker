package com.example.playlist_maker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.playlist.PlaylistDbEntity
import com.example.playlist_maker.data.database.track.TrackDao
import com.example.playlist_maker.data.database.track.TrackDbEntity

@Database(version = 1, entities =[TrackDbEntity::class, PlaylistDbEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}