package com.example.playlistmaker.playlist.ui.view_model

import com.example.playlistmaker.playlist.ui.model.PlaylistUi

sealed interface PlaylistsState {
    object Empty : PlaylistsState
    data class Content(val playlists: List<PlaylistUi>) : PlaylistsState
}