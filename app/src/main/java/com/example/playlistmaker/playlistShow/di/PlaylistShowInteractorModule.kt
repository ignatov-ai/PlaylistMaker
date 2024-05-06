package com.example.playlistmaker.playlistShow.di

import com.example.playlistmaker.playlistShow.domain.api.PlaylistShowInteractor
import com.example.playlistmaker.playlistShow.domain.impl.PlaylistShowInteractorImpl
import org.koin.dsl.module

val playlistShowInteractorModule = module {
    single<PlaylistShowInteractor> {
        PlaylistShowInteractorImpl(playlistShowRepository = get())
    }
}