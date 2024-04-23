package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.playlist.domain.api.PlaylistsRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }
}