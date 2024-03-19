package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.playlist.domain.impl.PlaylistsInteractorImpl
import org.koin.dsl.module

val playlistsInteractorModule = module {
    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(playlistsRepository = get())
    }
}