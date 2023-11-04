package com.example.playlistmaker.creator

import com.example.playlistmaker.data.old.RetrofitNetworkClient
import com.example.playlistmaker.data.old.TrackMapper
import com.example.playlistmaker.data.old.TracksRepositoryImpl
import com.example.playlistmaker.domain.old.TracksInteractor
import com.example.playlistmaker.domain.old.TracksRepository
import com.example.playlistmaker.domain.old.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), trackMapper = TrackMapper())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}