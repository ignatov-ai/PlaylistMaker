package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get())
    }
    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(historyStorage = get())
    }
}