package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.Track

interface TracksRepository {
    fun searchTrack(expression: String): List<Track>
}