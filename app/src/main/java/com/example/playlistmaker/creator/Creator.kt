package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.data.player.AndroidMediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(AndroidMediaPlayer())
    }

    fun provideTracksInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}