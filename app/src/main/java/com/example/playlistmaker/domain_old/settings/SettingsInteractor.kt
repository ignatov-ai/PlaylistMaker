package com.example.playlistmaker.domain_old.settings

import com.example.playlistmaker.domain_old.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}