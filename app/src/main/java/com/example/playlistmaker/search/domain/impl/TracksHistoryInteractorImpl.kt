package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository) :
    TracksHistoryInteractor {

    override fun getHistory(): Flow<List<Track>> {
        return repository.getHistory()
    }

    override fun saveHistory() {
        repository.saveHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }
}