package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface TracksHistoryInteractor {
    fun getHistory(): List<Track>
    fun saveHistory()
    fun clearHistory()
    fun addTrack(track: Track)
}