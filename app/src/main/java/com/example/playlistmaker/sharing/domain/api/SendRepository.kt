package com.example.playlistmaker.sharing.domain.api

interface SendRepository {
    fun doSend(text: String)
}