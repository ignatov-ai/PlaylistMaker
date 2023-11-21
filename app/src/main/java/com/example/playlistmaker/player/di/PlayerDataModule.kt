package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.Player
import com.example.playlistmaker.player.data.player.AndroidMediaPlayer
import org.koin.dsl.module

val playerDataModule = module {
    factory<Player> {
        AndroidMediaPlayer()
    }

    factory {
        MediaPlayer()
    }
}