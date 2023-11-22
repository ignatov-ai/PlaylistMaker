package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.ui.view_model.FavouritesViewModel
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaViewModelModule = module {
    viewModel {
        FavouritesViewModel()
    }
    viewModel {
        PlaylistViewModel()
    }
}