package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.ViewRepository

class ViewUseCase (private val viewRepository: ViewRepository) {
    fun execute(url: String){
        viewRepository.doView(url)
    }
}