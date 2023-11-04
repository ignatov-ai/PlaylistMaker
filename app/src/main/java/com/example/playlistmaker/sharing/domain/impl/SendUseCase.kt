package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SendRepository

class SendUseCase (private val sendRepository: SendRepository) {
    fun execute(text: String){
        sendRepository.doSend(text)
    }
}