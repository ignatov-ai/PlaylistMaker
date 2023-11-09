package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface TracksInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    fun interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}