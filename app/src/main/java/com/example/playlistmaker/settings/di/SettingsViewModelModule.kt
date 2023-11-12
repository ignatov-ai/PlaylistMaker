package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module {
    viewModel {
        SettingsViewModel(
            darkThemeInteractor = get(),
            send = get(),
            sendTo = get(),
            view = get()
        )
    }
}