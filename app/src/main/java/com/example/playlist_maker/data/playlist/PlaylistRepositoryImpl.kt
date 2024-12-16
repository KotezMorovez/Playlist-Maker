package com.example.playlist_maker.data.playlist

import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.playlist.playlistDbEntityToDomain
import com.example.playlist_maker.data.database.playlist.toDbEntity
import com.example.playlist_maker.data.database.playlist_to_track.PlaylistToTrackDao
import com.example.playlist_maker.data.database.track.toDomain
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.PlaylistRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistToTrackDao: PlaylistToTrackDao,
) : PlaylistRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPlaylistsSubscription(): Flow<List<Playlist>> {
        return withContext(Dispatchers.IO) {
            playlistDao.getAllPlaylists()
                .flatMapMerge { playlistDbEntityList ->

                    if (playlistDbEntityList.isEmpty()) {
                        return@flatMapMerge flowOf(listOf())
                    }

                    val flows = withContext(Dispatchers.IO) {
                        playlistDbEntityList.map { playlist ->
                            playlistToTrackDao.getTracksCountById(playlist.id)
                                .map { count ->
                                    Pair(playlist, count)
                                }
                        }
                    }

                    combine(flows) { array ->
                        array.toList()
                    }
                }.map { pairEntityToCountList ->
                    pairEntityToCountList.map { pair ->
                        playlistDbEntityToDomain(pair.first, pair.second)
                    }
                }
        }
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO){
            playlistDao.addPlaylist(playlist = playlist.toDbEntity())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPlaylistByIdSubscription(id: String): Flow<Pair<Playlist, List<Track>>> {
        return withContext(Dispatchers.IO) {
            playlistDao.getPlaylistById(id).flatMapMerge { playlistDbEntity ->
                playlistToTrackDao.getPlaylistTrackList(id).map { tracks ->
                    var duration = 0
                    tracks.forEach { trackDbEntity ->
                        duration += trackDbEntity.trackTimeMillis
                    }

                    val player = playlistDbEntityToDomain(playlistDbEntity, tracks.size, duration)
                    Pair(player, tracks.map { it.toDomain() })
                }
            }
        }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: String) {
        withContext(Dispatchers.IO){
            playlistToTrackDao.deleteTrackFromPlaylist(trackId, playlistId)
        }
    }

    override suspend fun deletePlaylist(id: String) {
        withContext(Dispatchers.IO){
            playlistToTrackDao.deletePlaylist(id)
            playlistDao.deletePlaylist(id)
        }
    }
}