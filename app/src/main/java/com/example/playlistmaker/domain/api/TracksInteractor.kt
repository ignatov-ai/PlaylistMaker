package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}