package com.example.playlistmaker.player.di

import org.koin.dsl.module

val playerModule = module {
    includes(
        playerDataModule,
        playerRepositoryModule,
        playerInteractorModule,
        playerViewModelModule
    )
}