package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.data.player.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.settings.data.DarkThemeRepositoryImpl
import com.example.playlistmaker.settings.data.SharedPrefsDarkTheme
import com.example.playlistmaker.settings.domain.api.DarkThemeInteractor
import com.example.playlistmaker.settings.domain.api.DarkThemeRepository
import com.example.playlistmaker.settings.domain.impl.DarkThemeInteractorImpl
import com.example.playlistmaker.sharing.data.SendRepositoryImpl
import com.example.playlistmaker.sharing.data.SendToRepositoryImpl
import com.example.playlistmaker.sharing.data.ViewRepositoryImpl
import com.example.playlistmaker.sharing.data.impl.SendImpl
import com.example.playlistmaker.sharing.data.impl.SendToImpl
import com.example.playlistmaker.sharing.data.impl.ViewImpl
import com.example.playlistmaker.sharing.domain.api.SendRepository
import com.example.playlistmaker.sharing.domain.api.SendToRepository
import com.example.playlistmaker.sharing.domain.api.ViewRepository

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(AndroidMediaPlayer())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSendUseCase(context: Context): SendRepository {
        return SendRepositoryImpl(SendImpl(context))
    }

    fun provideSendToUseCase(context: Context): SendToRepository {
        return SendToRepositoryImpl(SendToImpl(context))
    }

    fun provideViewUseCase(context: Context): ViewRepository {
        return ViewRepositoryImpl(ViewImpl(context))
    }

    private fun getDarkThemeRepository(context: Context): DarkThemeRepository {
        return DarkThemeRepositoryImpl(SharedPrefsDarkTheme(context))
    }

    fun provideDarkThemeInteractor(context: Context): DarkThemeInteractor {
        return DarkThemeInteractorImpl(getDarkThemeRepository(context))
    }


}