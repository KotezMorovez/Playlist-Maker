package com.example.playlist_maker.data.database.playlist_to_track

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker.data.database.track.TrackDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistToTrackDao {
    @Query("SELECT * FROM playlist_to_track WHERE playlist_id = :playlistId AND track_id = :trackId")
    suspend fun findTrackInPlaylist(playlistId: String, trackId: String): PlaylistToTrackDbEntity?

    @Insert(entity = PlaylistToTrackDbEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addTrackToPlaylist(entity: PlaylistToTrackDbEntity)

    @Query("SELECT COUNT(*) FROM playlist_to_track WHERE playlist_id = :id")
    fun getTracksCountById(id: String): Flow<Int>

    @Query("SELECT t.* FROM playlist_to_track pt JOIN tracks t ON pt.track_id = t.track_id WHERE pt.playlist_id = :id ORDER BY timestamp DESC")
    fun getPlaylistTrackList(id: String): Flow<List<TrackDbEntity>>

    @Query("DELETE FROM playlist_to_track WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: String)

    @Query("DELETE FROM playlist_to_track WHERE playlist_id = :id")
    suspend fun deletePlaylist(id: String)
}