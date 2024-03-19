package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>
}