package com.example.playlistmaker.favourites.di

import com.example.playlistmaker.favourite.di.favouriteInteractorModule
import com.example.playlistmaker.favourite.di.favouriteRepositoryModule
import org.koin.dsl.module

val favouriteModule = module {
    includes(
        favouritesViewModelModule,
        favouriteDataModule,
        favouriteInteractorModule,
        favouriteRepositoryModule
    )
}