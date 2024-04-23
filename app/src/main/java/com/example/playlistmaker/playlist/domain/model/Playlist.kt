package com.example.playlistmaker.playlist.domain.model

data class Playlist(
    val playlistId: Long? = null,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverUri: String,
    var countTracks: Int = 0
)