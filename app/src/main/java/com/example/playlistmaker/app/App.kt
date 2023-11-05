package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

const val THEME_PREFS = "THEME_PREFS"
const val DARK_THEME = "false"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val darkThemeInteractor = Creator.provideDarkThemeInteractor(this)
        switchTheme(darkThemeInteractor.darkThemeState())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}