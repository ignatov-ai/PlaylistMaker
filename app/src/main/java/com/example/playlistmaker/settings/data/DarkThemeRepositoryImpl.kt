package com.example.playlistmaker.settings.data

class DarkThemeRepositoryImpl(private val darkTheme: DarkTheme) : DarkTheme {
    override fun darkThemeState(): Boolean {
        return darkTheme.darkThemeState()
    }

    override fun darkThemeStateSave(isDarkTheme: Boolean) {
        darkTheme.darkThemeStateSave(isDarkTheme)
    }
}