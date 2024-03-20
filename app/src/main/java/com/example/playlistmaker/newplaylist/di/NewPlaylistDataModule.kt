package com.example.playlistmaker.newplaylist.di

import com.example.playlistmaker.newplaylist.data.ImagesStorage
import com.example.playlistmaker.newplaylist.data.storage.ExternalImagesStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val newPlaylistDataModule = module {
    single<ImagesStorage> {
        ExternalImagesStorage(context = androidContext())
    }
}