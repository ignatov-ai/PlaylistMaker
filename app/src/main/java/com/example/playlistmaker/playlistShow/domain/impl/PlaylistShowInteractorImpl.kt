package com.example.playlistmaker.playlistShow.domain.impl

import com.example.playlistmaker.playlistShow.domain.api.PlaylistShowInteractor
import com.example.playlistmaker.playlistShow.domain.api.PlaylistShowRepository
import com.example.playlistmaker.playlistShow.domain.model.PlaylistShow
import kotlinx.coroutines.flow.Flow

class PlaylistShowInteractorImpl (private val playlistShowRepository: PlaylistShowRepository) :
    PlaylistShowInteractor {
    override fun getPlaylistShow(playlistId: Long): Flow<PlaylistShow> {
        return playlistShowRepository.getPlaylistShow(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        playlistShowRepository.removeTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistShowRepository.deletePlaylist(playlistId)
    }
}