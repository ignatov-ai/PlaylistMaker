package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SendToRepository

class SendToUseCase (private val sendToRepository: SendToRepository) {
    fun execute(email: Array<String>, subject: String, text: String){
        sendToRepository.doSendTo(email, subject, text)
    }
}