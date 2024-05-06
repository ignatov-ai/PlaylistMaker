package com.example.playlistmaker.playlistEdit.di

import com.example.playlistmaker.playlistEdit.db.PlaylistEditRepositoryImpl
import com.example.playlistmaker.playlistEdit.domain.api.PlaylistEditRepository
import org.koin.dsl.module

val playlistEditRepositoryModule = module {
    single<PlaylistEditRepository> {
        PlaylistEditRepositoryImpl(database = get())
    }
}