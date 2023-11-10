package com.example.playlistmaker.sharing.data

interface SendTo {
    fun share(email: Array<String>, subject: String, text: String)
}