package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.domain.api.DarkThemeRepository

class DarkThemeRepositoryImpl(private val darkTheme: DarkTheme) : DarkThemeRepository {
    override fun darkThemeState(): Boolean {
        return darkTheme.darkThemeState()
    }

    override fun darkThemeStateSave(isDarkTheme: Boolean) {
        darkTheme.darkThemeStateSave(isDarkTheme)
    }
}