package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.favourites.di.favouriteModule
import com.example.playlistmaker.newplaylist.di.newPlaylistModule
import com.example.playlistmaker.player.di.playerModule
import com.example.playlistmaker.playlist.di.playlistModule
import com.example.playlistmaker.search.di.searchModule
import com.example.playlistmaker.settings.di.settingsModule
import com.example.playlistmaker.settings.domain.api.DarkThemeInteractor
import com.example.playlistmaker.sharing.di.sharingModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val THEME_PREFS = "THEME_PREFS"
const val DARK_THEME = "false"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                playerModule,
                favouriteModule,
                settingsModule,
                sharingModule,
                searchModule,
                playlistModule,
                newPlaylistModule
            )
        }

        val darkThemeInteractor: DarkThemeInteractor by inject()
        switchTheme(darkThemeInteractor.darkThemeState())

        PermissionRequester.initialize(applicationContext)
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