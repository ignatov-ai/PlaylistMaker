package com.example.playlistmaker.newplaylist.di

import org.koin.dsl.module

val newPlaylistModule = module {
    includes(
        newPlaylistDataModule,
        newPlaylistRepositoryModule,
        newPlaylistInteractorModule,
        newPlaylistViewModelModule
    )
}