package ru.kryu.playlistmaker.favourite.di

import com.example.playlistmaker.favourites.domain.FavouritesInteractor
import com.example.playlistmaker.favourites.domain.impl.FavouritesInteractorImpl
import org.koin.dsl.module

val favouriteInteractorModule = module {
    single<FavouritesInteractor> {
        FavouritesInteractorImpl(favouritesRepository = get())
    }
}