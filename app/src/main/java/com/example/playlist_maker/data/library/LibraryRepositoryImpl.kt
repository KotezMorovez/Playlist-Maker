package com.example.playlist_maker.data.library

import android.net.Uri
import com.example.playlist_maker.data.database.playlist.PlaylistDao
import com.example.playlist_maker.data.database.playlist.playlistDbEntityToDomain
import com.example.playlist_maker.data.database.playlist.toDbEntity
import com.example.playlist_maker.data.database.track.TrackDao
import com.example.playlist_maker.data.database.track.toDomain
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.domain.library.repository_api.LibraryRepository
import com.example.playlist_maker.domain.prefs.dto.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LibraryRepositoryImpl(
    private val trackDao: TrackDao,
    private val playlistDao: PlaylistDao,
    private val storageService: StorageService
) : LibraryRepository {
    override suspend fun getTracksSubscription(): Flow<List<Track>> {
        return withContext(Dispatchers.IO) {
            trackDao.getAllTracks().map {
                it.map { track ->
                    track.toDomain()
                }
            }
        }
    }

    override suspend fun getPlaylistsSubscription(): Flow<List<Playlist>> {
        return withContext(Dispatchers.IO) {
            playlistDao.getAllPlaylists().map {
                it.map { playlist ->
                    playlistDbEntityToDomain(playlist)
                }
            }
        }
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        return playlistDao.addPlaylist(playlist = playlist.toDbEntity())
    }

    override fun addImageToStorage(uri: Uri): Uri? {
        return storageService.addImageToStorage(uri)
    }
}