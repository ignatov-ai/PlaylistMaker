package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackResponse

interface NetworkClient {
    suspend fun searchTrackRequest(dto: Any): TrackResponse
}