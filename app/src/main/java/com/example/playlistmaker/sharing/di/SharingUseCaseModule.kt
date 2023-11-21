package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.domain.impl.SendToUseCase
import com.example.playlistmaker.sharing.domain.impl.SendUseCase
import com.example.playlistmaker.sharing.domain.impl.ViewUseCase
import org.koin.dsl.module

val sharingUseCaseModule = module {
    factory {
        SendUseCase(sendRepository = get())
    }
    factory {
        SendToUseCase(sendToRepository = get())
    }
    factory {
        ViewUseCase(viewRepository = get())
    }
}