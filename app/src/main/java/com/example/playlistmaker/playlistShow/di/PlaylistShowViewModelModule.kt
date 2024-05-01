package com.example.playlistmaker.playlistShow.di

import com.example.playlistmaker.playlistShow.ui.view_model.PlaylistShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistShowViewModelModule = module {
    viewModel { (playlistId: Long) ->
        PlaylistShowViewModel(
            playlistId = playlistId,
            playlistShowInteractor = get(),
            sendUseCase = get()
        )
    }
}