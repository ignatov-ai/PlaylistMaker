package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.ui.model.TrackUi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel { (track: TrackUi) ->
        PlayerViewModel(
            track = track,
            mediaPlayerInteractor = get(),
            favouritesInteractor = get(),
            playlistInteractor = get(),
            trackInPlaylistInteractor = get()
            )
    }
}