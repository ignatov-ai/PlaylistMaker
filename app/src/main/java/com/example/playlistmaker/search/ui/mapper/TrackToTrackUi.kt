package com.example.playlistmaker.search.ui.mapper

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.TrackUi

class TrackToTrackUi {
    fun map(track: Track): TrackUi {
        return TrackUi(
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
            artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            isFavourite = track.isFavourite
        )
    }
}