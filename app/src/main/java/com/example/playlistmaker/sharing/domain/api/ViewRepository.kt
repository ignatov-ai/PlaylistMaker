package com.example.playlistmaker.sharing.domain.api

interface ViewRepository {
    fun doView(url: String)
}