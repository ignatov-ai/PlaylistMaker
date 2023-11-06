package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.ui.model.Track

interface TracksInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}