package com.example.playlistmaker.settings.domain.api

interface DarkThemeRepository {
    fun darkThemeState(): Boolean
    fun darkThemeStateSave(isDarkTheme: Boolean)
}