package com.example.playlistmaker.player.domain.api

interface TrackInPlaylistRepository {
    suspend fun trackInDataBase(idTrack: Long): Boolean
    suspend fun addTrackInPlaylist(playlistId: Long, trackId: Long): Boolean
}