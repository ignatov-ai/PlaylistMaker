package com.example.playlistmaker.data.old

interface NetworkClient {
    fun doRequest(dto: Any): Response
}