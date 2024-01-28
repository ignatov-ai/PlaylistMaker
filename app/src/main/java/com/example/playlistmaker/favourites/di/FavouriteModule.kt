package com.example.playlistmaker.favourites.di

import org.koin.dsl.module

val favouriteModule = module {
    includes(
        mediaViewModelModule,
        favouriteDataModule
    )
}