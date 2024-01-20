package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {

    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>{
        return repository.searchTrack(expression).map { resourse ->
            when (resourse) {
                is Resource.Success -> {
                    Pair(resourse.data, null)
                }
                is Resource.Error -> {
                    Pair(null, resourse.message)
                }
            }
        }
    }
}