package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.storage.model.TrackStorage

interface HistoryStorage {
    fun getHistoryList(): List<TrackStorage>
    fun saveHistory(list: List<TrackStorage>)
    fun clearHistory()
}