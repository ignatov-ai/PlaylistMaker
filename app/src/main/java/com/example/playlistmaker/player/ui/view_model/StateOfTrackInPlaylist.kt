package com.example.playlistmaker.player.ui.view_model

sealed interface StateOfTrackInPlaylist{
    data class TrackInPlaylistAdded(val playlistName: String): StateOfTrackInPlaylist
    data class TrackInPlaylistNotAdded(val playlistName: String): StateOfTrackInPlaylist
}