package com.example.playlistmaker.newplaylist.domain

import com.example.playlistmaker.playlist.domain.model.Playlist

interface NewPlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun saveImageToPrivateStorage(path: String, imageId: String)
}