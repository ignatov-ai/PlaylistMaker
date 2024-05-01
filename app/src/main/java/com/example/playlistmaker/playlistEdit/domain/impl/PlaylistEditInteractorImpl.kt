package com.example.playlistmaker.playlistEdit.domain.impl

import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlistEdit.domain.api.PlaylistEditInteractor
import com.example.playlistmaker.playlistEdit.domain.api.PlaylistEditRepository

class PlaylistEditInteractorImpl(private val playlistEditRepository: PlaylistEditRepository) :
    PlaylistEditInteractor {
    override suspend fun playlistUpdate(playlist: Playlist) {
        playlistEditRepository.playlistUpdate(playlist)
    }
}