package com.example.playlistmaker.newplaylist.di

import com.example.playlistmaker.newplaylist.domain.NewPlaylistInteractor
import com.example.playlistmaker.newplaylist.domain.impl.NewPlaylistInteractorImpl
import org.koin.dsl.module

val newPlaylistInteractorModule = module {
    single<NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(get())
    }
}