package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTrack(expression)) {
                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }

                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }
            }
        }
    }

}