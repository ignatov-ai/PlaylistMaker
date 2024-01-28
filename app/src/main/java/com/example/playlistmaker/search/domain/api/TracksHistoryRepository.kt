package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksHistoryRepository {
    fun getHistory(): Flow<List<Track>>
    fun saveHistory()
    fun clearHistory()
    fun addTrack(track: Track)
}