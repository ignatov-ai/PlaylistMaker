package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.api.PlayerInteractor

interface Player {
    fun preparePlayer(dto: Any)
    fun startPlayer()
    fun pausePlayer()
    fun stopPlayer()
    fun currentPosition(): Int
    fun setOnPreparedListener(preparedListener: PlayerInteractor.PreparedListener)
    fun setOnCompletionListener(completionListener: PlayerInteractor.CompletionListener)
}