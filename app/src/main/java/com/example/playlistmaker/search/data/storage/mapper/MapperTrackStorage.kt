package com.example.playlistmaker.search.data.storage.mapper

import com.example.playlistmaker.search.data.storage.model.TrackStorage
import com.example.playlistmaker.search.domain.model.Track

class MapperTrackStorage {
    fun mapTrackStorageToDomain(trackStorage: TrackStorage): Track {
        return Track(
            trackId = trackStorage.trackId,
            trackName = trackStorage.trackName,
            artistName = trackStorage.artistName,
            trackTimeMillis = trackStorage.trackTimeMillis,
            artworkUrl100 = trackStorage.artworkUrl100,
            collectionName = trackStorage.collectionName,
            releaseDate = trackStorage.releaseDate,
            primaryGenreName = trackStorage.primaryGenreName,
            country = trackStorage.country,
            previewUrl = trackStorage.previewUrl,
        )
    }

    fun mapTrackStorage(track: Track): TrackStorage {
        return TrackStorage(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }
}
