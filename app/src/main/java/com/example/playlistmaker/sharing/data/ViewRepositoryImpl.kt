package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.api.ViewRepository

class ViewRepositoryImpl (private val view: View) : ViewRepository {
    override fun doView(url: String) {
        view.share(url)
    }
}