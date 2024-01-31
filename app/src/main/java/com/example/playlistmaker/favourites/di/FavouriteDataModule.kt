package com.example.playlistmaker.favourites.di

import androidx.room.Room
import com.example.playlistmaker.favourites.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favouriteDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}