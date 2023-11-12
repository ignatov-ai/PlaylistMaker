package com.example.playlistmaker.settings.di

import org.koin.dsl.module

val settingsModule = module {
    includes(
        settindsDataModule,
        settingsInteractorModule,
        settingsRepositoryModule,
        settingsViewModelModule
    )
}