package com.example.playlistmaker.domain.impl

import java.util.concurrent.Executors
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(expression))
        }
    }

}