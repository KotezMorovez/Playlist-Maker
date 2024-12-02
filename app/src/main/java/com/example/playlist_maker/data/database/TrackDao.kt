package com.example.playlist_maker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackDbEntity)

    @Delete(entity = TrackDbEntity::class)
    fun deleteTrack(track: TrackDbEntity)

    @Query("SELECT * FROM favourite_tracks")
    fun getAllTracks(): Flow<List<TrackDbEntity>>

    @Query("SELECT * FROM favourite_tracks WHERE track_id = :id")
    fun findTrackInTable(id: String): TrackDbEntity?
}