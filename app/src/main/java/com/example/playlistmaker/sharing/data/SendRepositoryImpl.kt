package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.api.SendRepository

class SendRepositoryImpl (private val send: Send) : SendRepository {
    override fun doSend(text: String) {
        send.share(text)
    }
}