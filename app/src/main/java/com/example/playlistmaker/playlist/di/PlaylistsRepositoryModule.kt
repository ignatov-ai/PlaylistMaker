package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.data.PlaylistsRepositoryImpl
import com.example.playlistmaker.playlist.domain.api.PlaylistsRepository
import org.koin.dsl.module

val playlistsRepositoryModule = module {
    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(database = get())
    }
}