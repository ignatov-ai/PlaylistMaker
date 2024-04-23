package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.data.TrackInPlaylistRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.api.TrackInPlaylistRepository
import org.koin.dsl.module

val playerRepositoryModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl(player = get())
    }

    single<TrackInPlaylistRepository> {
        TrackInPlaylistRepositoryImpl(database = get())
    }
}