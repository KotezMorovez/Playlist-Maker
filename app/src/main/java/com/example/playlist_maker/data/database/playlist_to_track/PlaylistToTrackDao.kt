package com.example.playlist_maker.data.database.playlist_to_track

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistToTrackDao {
    @Query("SELECT * FROM playlist_to_track WHERE playlist_id = :playlistId AND track_id = :trackId")
    fun findTrackInPlaylist(playlistId: String, trackId: String): PlaylistToTrackDbEntity?

    @Insert(entity = PlaylistToTrackDbEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTrackToPlaylist(entity: PlaylistToTrackDbEntity)

    @Query("SELECT COUNT(*) FROM playlist_to_track WHERE playlist_id = :id")
    fun getTracksCountById(id: String): Flow<Int>
}