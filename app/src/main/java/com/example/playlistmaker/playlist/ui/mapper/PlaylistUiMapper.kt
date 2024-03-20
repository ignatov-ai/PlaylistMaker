package com.example.playlistmaker.playlist.ui.mapper

import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlist.ui.model.PlaylistUi

object PlaylistUiMapper {
    fun map(playlist: Playlist): PlaylistUi {
        return PlaylistUi(
            playlistId = playlist.playlistId!!,
            playlistImage = playlist.playlistCoverUri,
            playlistName = playlist.playlistName,
            playlistTracksNumber = playlist.countTracks
        )
    }

    fun map(playlist: PlaylistUi): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistCoverUri = playlist.playlistImage,
            playlistName = playlist.playlistName,
            countTracks = playlist.playlistTracksNumber,
            playlistDescription = ""
        )
    }
}