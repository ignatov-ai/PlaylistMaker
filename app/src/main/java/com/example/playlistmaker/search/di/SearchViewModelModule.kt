package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.mapper.TrackUiMapper
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(
            trackInteractor = get(),
            trackHistoryInteractor = get()
        )
    }

    single { TrackUiMapper }

    single { TrackUiMapper }
}