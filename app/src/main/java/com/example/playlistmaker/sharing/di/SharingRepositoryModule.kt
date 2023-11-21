package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.SendRepositoryImpl
import com.example.playlistmaker.sharing.data.SendToRepositoryImpl
import com.example.playlistmaker.sharing.data.ViewRepositoryImpl
import com.example.playlistmaker.sharing.domain.api.SendRepository
import com.example.playlistmaker.sharing.domain.api.SendToRepository
import com.example.playlistmaker.sharing.domain.api.ViewRepository
import org.koin.dsl.module

val sharingRepositoryModule = module {
    single<SendRepository> {
        SendRepositoryImpl(send = get())
    }
    single<SendToRepository> {
        SendToRepositoryImpl(sendTo = get())
    }
    single<ViewRepository> {
        ViewRepositoryImpl(view = get())
    }
}