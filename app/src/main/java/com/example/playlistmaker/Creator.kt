package com.example.playlistmaker

import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.TrackMapper
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient(), trackMapper = TrackMapper())
    }

    fun provideTracksInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }
}