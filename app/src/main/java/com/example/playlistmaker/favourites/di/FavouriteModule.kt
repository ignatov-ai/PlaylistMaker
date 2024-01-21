package com.example.playlistmaker.favourites.di

import org.koin.dsl.module

val mediaModule = module {
    includes(
        mediaViewModelModule,
        favouriteDataModule
        )
}