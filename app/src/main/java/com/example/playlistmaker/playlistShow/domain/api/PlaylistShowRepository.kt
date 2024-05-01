package com.example.playlistmaker.playlistShow.domain.api

import com.example.playlistmaker.playlistShow.domain.model.PlaylistShow
import kotlinx.coroutines.flow.Flow

interface PlaylistShowRepository {

    fun getPlaylistShow(playlistId: Long): Flow<PlaylistShow>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)
    suspend fun deletePlaylist(playlistId: Long)
}