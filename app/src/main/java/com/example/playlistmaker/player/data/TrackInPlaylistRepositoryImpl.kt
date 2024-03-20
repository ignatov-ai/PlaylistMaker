package com.example.playlistmaker.player.data

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.newplaylist.data.db.PlaylistTrackEntity
import com.example.playlistmaker.player.domain.api.TrackInPlaylistRepository

class TrackInPlaylistRepositoryImpl(private val database: AppDatabase) : TrackInPlaylistRepository {
    override suspend fun trackInDataBase(idTrack: Long): Boolean {
        return database.trackInPlaylistDao().existsTrackInDb(idTrack)
    }

    override suspend fun addTrackInPlaylist(playlistId: Long, trackId: Long): Boolean {
        val isTrackInPlaylist =
            database.trackInPlaylistDao()
                .existsTrackInPlaylist(playlistId, trackId)
        return if (isTrackInPlaylist) {
            false
        } else {
            database.trackInPlaylistDao().addTrackInPlaylist(
                PlaylistTrackEntity(
                    playlistId = playlistId,
                    trackId = trackId
                )
            )
            return true
        }
    }
}