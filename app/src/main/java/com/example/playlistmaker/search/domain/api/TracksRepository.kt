package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface TracksRepository {
    fun searchTrack(expression: String): Flow<Resource<List<Track>>>
}