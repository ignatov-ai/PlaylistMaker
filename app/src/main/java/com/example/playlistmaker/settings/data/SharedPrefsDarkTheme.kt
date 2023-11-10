package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.app.DARK_THEME
import com.example.playlistmaker.app.THEME_PREFS

class SharedPrefsDarkTheme(private val context: Context) : DarkTheme {
    override fun darkThemeState(): Boolean {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
        return sharedPrefs.getBoolean(DARK_THEME, false)
    }

    override fun darkThemeStateSave(isDarkTheme: Boolean) {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME, isDarkTheme)
            .apply()
    }
}