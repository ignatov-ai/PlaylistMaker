package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.TrackInPlaylistInteractor
import com.example.playlistmaker.player.domain.api.TrackInPlaylistRepository

class TrackInPlaylistInteractorImpl(private val trackInPlaylistRepository: TrackInPlaylistRepository) :
    TrackInPlaylistInteractor {
    override suspend fun trackInDataBase(idTrack: Long): Boolean {
        return trackInPlaylistRepository.trackInDataBase(idTrack)
    }

    override suspend fun addTrackInPlaylist(playlistId: Long, trackId: Long): Boolean {
        return trackInPlaylistRepository.addTrackInPlaylist(playlistId, trackId)
    }

}