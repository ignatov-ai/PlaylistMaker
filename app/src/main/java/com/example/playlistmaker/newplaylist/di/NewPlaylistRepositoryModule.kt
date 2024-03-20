package com.example.playlistmaker.newplaylist.di

import com.example.playlistmaker.newplaylist.data.NewPlaylistRepositoryImpl
import com.example.playlistmaker.newplaylist.domain.NewPlaylistRepository
import org.koin.dsl.module

val newPlaylistRepositoryModule = module {
    single<NewPlaylistRepository> {
        NewPlaylistRepositoryImpl(database = get(), imagesStorage = get())
    }
}