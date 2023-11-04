package com.example.playlistmaker.domain_old.old

import com.example.playlistmaker.search.ui.model.Track

interface TracksRepository {
    fun searchTrack(expression: String): List<Track>
}