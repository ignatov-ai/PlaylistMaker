package com.example.playlistmaker.playlist.ui.model

data class PlaylistUi(
    val playlistId: Long,
    val playlistImage: String,
    val playlistName: String,
    val playlistTracksNumber: Int
) {
    fun getPlaylistTracksNumberText(): String {
        var lastNumbers = playlistTracksNumber % 100
        if (lastNumbers in 11..14) {
            return "$playlistTracksNumber треков"
        } else {
            lastNumbers = playlistTracksNumber % 10
            when (lastNumbers) {
                1 -> return "$playlistTracksNumber трек"
                in 2..4 -> return "$playlistTracksNumber трека"
                else -> return "$playlistTracksNumber треков"
            }
        }
    }
}
