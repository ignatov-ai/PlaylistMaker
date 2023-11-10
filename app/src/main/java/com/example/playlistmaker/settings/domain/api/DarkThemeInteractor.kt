package com.example.playlistmaker.settings.domain.api

interface DarkThemeInteractor {
    fun darkThemeState(): Boolean
    fun darkThemeStateSave(isDarkTheme: Boolean)
}