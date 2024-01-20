package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val trackInteractorModule = module {
    factory<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(repository = get())
    }
    factory<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }
}