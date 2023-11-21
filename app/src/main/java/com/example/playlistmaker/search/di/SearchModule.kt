package com.example.playlistmaker.search.di

import org.koin.dsl.module

val searchModule = module {
    includes(
        searchDataModule,
        searchRepositoryModule,
        trackInteractorModule,
        searchViewModelModule
    )
}