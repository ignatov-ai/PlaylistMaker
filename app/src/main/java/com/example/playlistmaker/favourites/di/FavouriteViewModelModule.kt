package com.example.playlistmaker.favourites.di

import com.example.playlistmaker.favourites.ui.view_model.FavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesViewModelModule = module {
    viewModel {
        FavouritesViewModel(
            favouritesInteractor = get()
        )
    }
}