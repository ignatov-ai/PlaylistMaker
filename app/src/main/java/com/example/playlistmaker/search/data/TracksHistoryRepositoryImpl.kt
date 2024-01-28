package com.example.playlistmaker.search.data

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.search.data.mapper.TrackDtoToDomain
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.storage.mapper.MapperTrackStorage
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TracksHistoryRepositoryImpl(
    private val historyStorage: HistoryStorage,
    private val database: AppDatabase
    ): TracksHistoryRepository {

    companion object {
        private const val HISTORY_TRACK_COUNT = 10
    }

    private val trackHistory = getTrackHistoryFromStorage().toMutableList()

    private fun getTrackHistoryFromStorage(): List<Track> {
        return historyStorage.getHistoryList()
            .map { MapperTrackStorage().mapTrackStorageToDomain(it) }
    }

    init {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        scope.launch {
            trackHistory.addAll(historyStorage.getHistoryList()
                .map { MapperTrackStorage().mapTrackStorageToDomain(it) })
            val favouriteTracks = database.trackDao().getIdTracks()
            trackHistory.map { track ->
                if (favouriteTracks.contains(track.trackId)) {
                    track.isFavourite = true
                }
            }
        }
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