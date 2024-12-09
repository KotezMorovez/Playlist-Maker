package com.example.playlist_maker.data.playlist

import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.playlist.playlistDbEntityToDomain
import com.example.playlist_maker.data.database.playlist.toDbEntity
import com.example.playlist_maker.data.database.playlist_to_track.PlaylistToTrackDao
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistToTrackDao: PlaylistToTrackDao,
): PlaylistRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPlaylistsSubscription(): Flow<List<Playlist>> {
        return withContext(Dispatchers.IO) {
            playlistDao.getAllPlaylists()
                .flatMapMerge { playlistDbEntityList ->

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
        return playlistDao.addPlaylist(playlist = playlist.toDbEntity())
    }
}