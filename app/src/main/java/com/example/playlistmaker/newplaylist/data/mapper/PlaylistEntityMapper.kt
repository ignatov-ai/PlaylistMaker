package com.example.playlistmaker.newplaylist.data.mapper

import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity
import com.example.playlistmaker.playlist.domain.model.Playlist

object PlaylistEntityMapper {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCoverUri = playlist.playlistCoverUri
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId ?: 0,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCoverUri = playlist.playlistCoverUri
        )
    }
}