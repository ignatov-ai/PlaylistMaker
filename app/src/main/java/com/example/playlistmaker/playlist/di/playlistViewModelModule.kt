package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModelModule = module {
    viewModel {
        PlaylistViewModel()
    }
}