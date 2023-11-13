package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.ui.view_model.MediaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaViewModelModule = module {
    viewModel {
        MediaViewModel()
    }
}