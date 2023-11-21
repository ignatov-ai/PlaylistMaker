package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.DarkThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.DarkThemeRepository
import org.koin.dsl.module

val settingsRepositoryModule = module {
    single<DarkThemeRepository> {
        DarkThemeRepositoryImpl(darkTheme = get())
    }
}