package com.example.playlistmaker.presentations.tracks

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track

class TrackPresenter: TracksInteractor.TrackConsumer {
    private val tracks = ArrayList<Track>()
    override fun consume(foundTrack: List<Track>) {
        tracks.addAll(foundTrack)
    }

    fun getTrackList():ArrayList<Track>{
        return tracks
    }
}