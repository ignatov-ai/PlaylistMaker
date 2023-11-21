package com.example.playlistmaker.sharing.di

import org.koin.dsl.module

val sharingModule = module {
    includes(
        sharingDataModule,
        sharingRepositoryModule,
        sharingUseCaseModule,
    )
}