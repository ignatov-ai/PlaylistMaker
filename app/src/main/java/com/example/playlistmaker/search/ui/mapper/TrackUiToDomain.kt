package com.example.playlistmaker.search.ui.mapper

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.TrackUi

class TrackUiToDomain {
    fun map(trackUi: TrackUi): Track {
        return Track(
            trackId = trackUi.trackId,
            trackName = trackUi.trackName,
            artistName = trackUi.artistName,
            trackTimeMillis = trackUi.trackTimeMillis,
            artworkUrl100 = trackUi.artworkUrl100,
            collectionName = trackUi.collectionName,
            releaseDate = trackUi.releaseDate,
            primaryGenreName = trackUi.primaryGenreName,
            country = trackUi.country,
            previewUrl = trackUi.previewUrl,
            isFavourite = trackUi.isFavourite
        )
    }
}