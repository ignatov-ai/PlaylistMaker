package com.example.playlistmaker.favourite.di

import com.example.playlistmaker.favourites.data.FavouritesRepositoryImpl
import com.example.playlistmaker.favourites.domain.FavouritesRepository
import org.koin.dsl.module

val favouriteRepositoryModule = module {
    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get())
    }
}