package com.example.playlistmaker.settings.data

interface DarkTheme {
    fun darkThemeState(): Boolean
    fun darkThemeStateSave(isDarkTheme: Boolean)
}