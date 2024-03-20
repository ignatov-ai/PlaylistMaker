package com.example.playlistmaker.newplaylist.data.mapper

import com.example.playlistmaker.favourites.data.db.TrackEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistTrackEntity
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track

object PlaylistTrackEntityMapper {
    fun map(playlist: PlaylistEntity, track: TrackEntity): PlaylistTrackEntity {
        return PlaylistTrackEntity(playlistId = playlist.playlistId, trackId = track.trackId)
    }

    fun map(playlist: Playlist, track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(playlistId = playlist.playlistId!!, trackId = track.trackId)
    }
}