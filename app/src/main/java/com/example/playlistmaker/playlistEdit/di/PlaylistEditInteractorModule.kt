package com.example.playlistmaker.playlistEdit.di

import com.example.playlistmaker.playlistEdit.domain.api.PlaylistEditInteractor
import com.example.playlistmaker.playlistEdit.domain.impl.PlaylistEditInteractorImpl
import org.koin.dsl.module

val playlistEditInteractorModule = module {
    single<PlaylistEditInteractor> {
        PlaylistEditInteractorImpl(playlistEditRepository = get())
    }
}