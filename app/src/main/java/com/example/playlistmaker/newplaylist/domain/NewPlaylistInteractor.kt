package com.example.playlistmaker.newplaylist.domain

import com.example.playlistmaker.playlist.domain.model.Playlist

interface NewPlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun saveImageToStorage(path: String, imageId: String)
}