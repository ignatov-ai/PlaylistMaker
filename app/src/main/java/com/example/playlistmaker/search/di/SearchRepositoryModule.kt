package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get(), database = get())
    }
    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(historyStorage = get(), database = get())
    }
}