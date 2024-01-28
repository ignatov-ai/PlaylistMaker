package com.example.playlistmaker.favourites.di

import org.koin.dsl.module
import ru.kryu.playlistmaker.favourite.di.favouriteInteractorModule
import ru.kryu.playlistmaker.favourite.di.favouriteRepositoryModule

val favouriteModule = module {
    includes(
        mediaViewModelModule,
        favouriteDataModule,
        favouriteInteractorModule,
        favouriteRepositoryModule
    )
}