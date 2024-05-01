package com.example.playlistmaker.playlistShow.di

import org.koin.dsl.module

val playlistMainModule = module {
    includes(
        playlistShowRepositoryModule,
        playlistShowInteractorModule,
        playlistShowViewModelModule,
    )
}