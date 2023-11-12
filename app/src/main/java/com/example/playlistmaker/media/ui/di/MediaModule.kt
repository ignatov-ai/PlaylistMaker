package com.example.playlistmaker.media.ui.di

import org.koin.dsl.module

val mediaModule = module {
    includes(
        mediaViewModelModule,

        )
}