package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.api.SendToRepository

class SendToRepositoryImpl (private val sendTo: SendTo) : SendToRepository {
    override fun doSendTo(email: Array<String>, subject: String, text: String) {
        sendTo.share(email, subject, text)
    }

}