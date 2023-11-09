package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.data.player.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.storage.SharedPrefsHistory
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
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
import com.example.playlistmaker.sharing.domain.impl.SendToUseCase
import com.example.playlistmaker.sharing.domain.impl.SendUseCase
import com.example.playlistmaker.sharing.domain.impl.ViewUseCase

object Creator {

    // проигрыватель
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(AndroidMediaPlayer())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    // Поиск трека
    private fun getTrackRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository(context))
    }

    // история поиска
    private fun getTrackHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(SharedPrefsHistory(context))
    }

    fun provideTracksHistoryInteractor(context: Context): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(getTrackHistoryRepository(context))
    }

    // обраблотчики экрана настроек
    private fun SendRepository(context: Context): SendRepository {
        return SendRepositoryImpl(SendImpl(context))
    }

    fun provideSendUseCase(context: Context): SendUseCase {
        return SendUseCase(SendRepository(context))
    }

    private fun SendToRepository(context: Context): SendToRepository {
        return SendToRepositoryImpl(SendToImpl(context))
    }

    fun provideSendToUseCase(context: Context): SendToUseCase {
        return SendToUseCase(SendToRepository(context))
    }

    private fun ViewRepository(context: Context): ViewRepository {
        return ViewRepositoryImpl(ViewImpl(context))
    }

    fun provideViewUseCase(context: Context): ViewUseCase {
        return ViewUseCase(ViewRepository(context))
    }

    private fun getDarkThemeRepository(context: Context): DarkThemeRepository {
        return DarkThemeRepositoryImpl(SharedPrefsDarkTheme(context))
    }

    fun provideDarkThemeInteractor(context: Context): DarkThemeInteractor {
        return DarkThemeInteractorImpl(getDarkThemeRepository(context))
    }
}