package com.example.playlistmaker.playlistEdit.di

import org.koin.dsl.module

val playlistEditModule = module {
    includes(
        playlistEditRepositoryModule,
        playlistEditInteractorModule,
        playlistEditViewModelModule
    )
}