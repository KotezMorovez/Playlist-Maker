package com.example.playlist_maker.data.database.track

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
    suspend fun deleteTrack(track: TrackDbEntity)

    @Query("SELECT * FROM tracks WHERE is_favourite = 1 ORDER BY timestamp_favourite DESC")
    fun getAllFavouriteTracks(): Flow<List<TrackDbEntity>>

    @Query("SELECT * FROM tracks WHERE is_favourite = 1 AND track_id = :id")
    suspend fun findTrackInFavourites(id: String): TrackDbEntity?
}