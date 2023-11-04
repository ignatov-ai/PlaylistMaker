package com.example.playlistmaker.old

import com.example.playlistmaker.domain_old.old.TracksInteractor
import com.example.playlistmaker.search.ui.model.Track

class TrackPresenter: TracksInteractor.TrackConsumer {
    private val tracks = ArrayList<Track>()
    override fun consume(foundTrack: List<Track>) {
        tracks.addAll(foundTrack)
    }

    fun getTrackList():ArrayList<Track>{
        return tracks
    }
}