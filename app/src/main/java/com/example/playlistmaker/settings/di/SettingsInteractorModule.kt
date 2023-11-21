package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.api.DarkThemeInteractor
import com.example.playlistmaker.settings.domain.impl.DarkThemeInteractorImpl
import org.koin.dsl.module

val settingsInteractorModule = module {
    factory<DarkThemeInteractor> {
        DarkThemeInteractorImpl(darkThemeRepository = get())
    }
}