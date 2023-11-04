package com.example.playlistmaker.sharing.domain.api

interface SendToRepository {
    fun doSendTo(email: Array<String>, subject: String, text: String)
}