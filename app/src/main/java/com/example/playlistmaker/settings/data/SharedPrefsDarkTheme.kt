package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import com.example.playlistmaker.app.DARK_THEME

class SharedPrefsDarkTheme(private val sharedPrefs: SharedPreferences) : DarkTheme {
    override fun darkThemeState(): Boolean {
        return sharedPrefs.getBoolean(DARK_THEME, false)
    }

    override fun darkThemeStateSave(isDarkTheme: Boolean) {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME, isDarkTheme)
            .apply()
    }
}