package com.example.playlistmaker.newplaylist.di

import com.example.playlistmaker.newplaylist.ui.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPlaylistViewModelModule = module {
    viewModel {
        NewPlaylistViewModel(get())
    }
}
