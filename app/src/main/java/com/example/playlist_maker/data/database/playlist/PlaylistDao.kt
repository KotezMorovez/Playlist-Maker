package com.example.playlist_maker.data.database.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistDbEntity)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistDbEntity>>

//    @Query("SELECT COUNT(*) FROM tracks WHERE playlist_id = id")
//    fun getTracksCountById(id: String)

//    @Insert(entity = TrackListDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addTrackIntoPlaylist(id: Int, playlistId: String, trackId: String, timestamp: Long)
}