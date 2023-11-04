package com.example.playlistmaker.data.old

import com.example.playlistmaker.domain_old.old.TracksRepository
import com.example.playlistmaker.search.ui.model.Track

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