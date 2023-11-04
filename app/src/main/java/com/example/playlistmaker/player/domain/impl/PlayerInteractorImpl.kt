package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.domain_old.old.TracksRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun stopPlayer() {
        repository.stopPlayer()
    }

    override fun currentPosition(): Int {
        return repository.currentPosition()
    }

    override fun setOnPreparedListener(preparedListener: PlayerInteractor.PreparedListener) {
        repository.setOnPreparedListener(preparedListener)
    }

    override fun setOnCompletionListener(completionListener: PlayerInteractor.CompletionListener) {
        repository.setOnCompletionListener(completionListener)
    }
}