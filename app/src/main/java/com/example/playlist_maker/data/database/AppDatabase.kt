package com.example.playlist_maker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities =[TrackDbEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}