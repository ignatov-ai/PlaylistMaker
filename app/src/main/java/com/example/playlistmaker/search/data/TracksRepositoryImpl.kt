package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackDtoToDomain
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    TrackDtoToDomain().map(it)
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}