package com.example.playlistmaker.playlistShow.di

import com.example.playlistmaker.playlistShow.data.db.PlaylistShowRepositoryImpl
import com.example.playlistmaker.playlistShow.domain.api.PlaylistShowRepository
import org.koin.dsl.module

val playlistShowRepositoryModule = module {
    single<PlaylistShowRepository> {
        PlaylistShowRepositoryImpl(database = get())
    }
}