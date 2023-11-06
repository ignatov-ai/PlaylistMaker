package com.example.playlistmaker.search.data.storage

import com.example.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}