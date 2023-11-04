package com.example.playlistmaker.domain.old

interface TracksRepository {
    fun searchTrack(expression: String): List<Track>
}