package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.DarkThemeInteractor
import com.example.playlistmaker.settings.domain.api.DarkThemeRepository

class DarkThemeInteractorImpl(private val darkThemeRepository: DarkThemeRepository) : DarkThemeInteractor {
    override fun darkThemeState(): Boolean {
        return darkThemeRepository.darkThemeState()
    }

    override fun darkThemeStateSave(isDarkTheme: Boolean) {
        darkThemeRepository.darkThemeStateSave(isDarkTheme)
    }
}