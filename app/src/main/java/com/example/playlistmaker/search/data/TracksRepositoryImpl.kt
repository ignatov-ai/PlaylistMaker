package com.example.playlistmaker.search.data

import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackDtoToDomain
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTrack(trackName: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.searchTrackRequest(TrackSearchRequest(trackName))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(R.string.connectionErrorString.toString()))
            }

            200 -> {
                val resource = Resource.Success((response as TrackSearchResponse).results.map {
                    TrackDtoToDomain().map(it)
                })
                emit(resource)
            }

            else -> {
                emit(Resource.Error(R.string.connectionErrorString.toString()))
            }
        }
    }
}