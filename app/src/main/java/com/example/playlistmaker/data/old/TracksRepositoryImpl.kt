package com.example.playlistmaker.data.old

import com.example.playlistmaker.domain.old.TracksRepository
import com.example.playlistmaker.domain.old.Track

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