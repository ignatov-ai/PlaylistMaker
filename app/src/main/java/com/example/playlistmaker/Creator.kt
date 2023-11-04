package com.example.playlistmaker

import com.example.playlistmaker.data.RetrofitNetworkClient
import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.domain.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), trackMapper = TrackMapper())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}