package com.example.playlistmaker.playlistEdit.domain.api

import com.example.playlistmaker.playlist.domain.model.Playlist

interface PlaylistEditInteractor {
    suspend fun playlistUpdate(playlist: Playlist)
}