package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackDtoToDomain
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    companion object{
        const val CONNECTION_ERROR = "Проверьте подключение к интернету"
        const val SERVER_ERROR = "Ошибка сервера"
    }

    override fun searchTrack(trackName: String): Resource<List<Track>> {
        val response = networkClient.searchTrackRequest(TrackSearchRequest(trackName))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(CONNECTION_ERROR)
            }

            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    TrackDtoToDomain().map(it)
                })
            }

            else -> {
                Resource.Error(SERVER_ERROR)
            }
        }
    }
}