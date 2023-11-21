package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.storage.mapper.MapperTrackStorage
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.model.Track

class TracksHistoryRepositoryImpl(private val historyStorage: HistoryStorage) :
    TracksHistoryRepository {

    companion object {
        private const val HISTORY_TRACK_COUNT = 9
    }

    private val trackHistory = getTrackHistoryFromStorage().toMutableList()

    private fun getTrackHistoryFromStorage(): List<Track> {
        return historyStorage.getHistoryList()
            .map { MapperTrackStorage().mapTrackStorageToDomain(it) }
    }

    override fun getHistory(): List<Track> {
        return trackHistory
    }

    override fun saveHistory() {
        historyStorage.saveHistory(trackHistory.map {
            MapperTrackStorage().mapTrackStorage(it)
        })
    }

    override fun clearHistory() {
        historyStorage.clearHistory()
        trackHistory.clear()
    }

    override fun addTrack(track: Track) {
        trackHistory.removeIf { it.trackId == track.trackId }
        trackHistory.add(0, track)
        if (trackHistory.size > HISTORY_TRACK_COUNT) trackHistory.removeLast()
    }
}