package com.example.playlist_maker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(entity = TrackDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackDbEntity)

    @Delete(entity = TrackDbEntity::class)
    fun deleteTrack(track: TrackDbEntity)

    @Query("SELECT * FROM favourite_tracks_table")
    fun getAllTracks(): Flow<List<TrackDbEntity>>

    @Query("SELECT * FROM favourite_tracks_table WHERE trackId = :id")
    fun findTrackInTable(id: String): List<Track>
}