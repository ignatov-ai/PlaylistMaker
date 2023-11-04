package com.example.playlistmaker.domain_old.old

import com.example.playlistmaker.search.ui.model.Track

interface TracksInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}