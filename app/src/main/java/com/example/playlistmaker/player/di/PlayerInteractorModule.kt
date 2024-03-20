package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.TrackInPlaylistInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.impl.TrackInPlaylistInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }

    single<TrackInPlaylistInteractor> {
        TrackInPlaylistInteractorImpl(trackInPlaylistRepository = get())
    }
}