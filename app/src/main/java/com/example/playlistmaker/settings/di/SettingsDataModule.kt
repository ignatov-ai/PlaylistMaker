package com.example.playlistmaker.settings.di

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.app.THEME_PREFS
import com.example.playlistmaker.settings.data.DarkTheme
import com.example.playlistmaker.settings.data.SharedPrefsDarkTheme
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settindsDataModule = module {
    single<DarkTheme> {
        SharedPrefsDarkTheme(context = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            THEME_PREFS,
            Application.MODE_PRIVATE
        )
    }
}