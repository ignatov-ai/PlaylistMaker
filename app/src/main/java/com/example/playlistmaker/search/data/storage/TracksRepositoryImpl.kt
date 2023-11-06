package com.example.playlistmaker.search.data.storage

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.model.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val trackMapper: TrackMapper):
    TracksRepository {

    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200){
            return (response as TrackSearchResponse).results.map { trackDto ->
                trackMapper.mapToDomain(trackDto)
            }
        } else {
            return emptyList()
        }
    }
}