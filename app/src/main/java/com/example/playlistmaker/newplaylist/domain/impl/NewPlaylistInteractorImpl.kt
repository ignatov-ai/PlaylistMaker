package com.example.playlistmaker.newplaylist.domain.impl

import com.example.playlistmaker.newplaylist.domain.NewPlaylistInteractor
import com.example.playlistmaker.newplaylist.domain.NewPlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist

class NewPlaylistInteractorImpl (private val createPlaylistRepository: NewPlaylistRepository) :
    NewPlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        createPlaylistRepository.createPlaylist(playlist)
    }

    override suspend fun saveImageToStorage(path: String, imageId: String) {
        createPlaylistRepository.saveImageToPrivateStorage(path, imageId)
    }
}