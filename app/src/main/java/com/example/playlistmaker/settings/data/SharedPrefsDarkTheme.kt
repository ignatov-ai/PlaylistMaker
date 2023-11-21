package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.app.DARK_THEME
import com.example.playlistmaker.app.THEME_PREFS

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