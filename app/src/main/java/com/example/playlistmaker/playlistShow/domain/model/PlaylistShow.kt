package com.example.playlistmaker.playlistShow.domain.model

import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track

data class PlaylistShow(
    val playlist: Playlist,
    val tracks: List<Track>
)
