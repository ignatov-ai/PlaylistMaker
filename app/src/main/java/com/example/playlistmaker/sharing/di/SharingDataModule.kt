package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.Send
import com.example.playlistmaker.sharing.data.SendTo
import com.example.playlistmaker.sharing.data.ViewAgreement
import com.example.playlistmaker.sharing.data.impl.SendImpl
import com.example.playlistmaker.sharing.data.impl.SendToImpl
import com.example.playlistmaker.sharing.data.impl.ViewAgreementImpl
import org.koin.dsl.module

val sharingDataModule = module {
    single<Send> {
        SendImpl(context = get())
    }

    single<SendTo> {
        SendToImpl(context = get())
    }

    single<ViewAgreement> {
        ViewAgreementImpl(context = get())
    }
}