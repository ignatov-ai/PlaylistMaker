package com.example.playlistmaker

import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.Track

class TrackPresenter: TracksInteractor.TrackConsumer {
    private val tracks = ArrayList<Track>()
    override fun consume(foundTrack: List<Track>) {
        tracks.addAll(foundTrack)
    }

    fun getTrackList():ArrayList<Track>{
        return tracks
    }
}