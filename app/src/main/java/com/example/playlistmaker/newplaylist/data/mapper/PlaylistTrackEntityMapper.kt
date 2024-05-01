package com.example.playlistmaker.newplaylist.data.mapper

import com.example.playlistmaker.favourites.data.db.TrackEntity
import com.example.playlistmaker.favourites.data.mapper.TrackDbMapper
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistTrackEntity
import com.example.playlistmaker.newplaylist.data.db.dao.PlaylistWithTracks
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlistShow.domain.model.PlaylistShow
import com.example.playlistmaker.search.domain.model.Track

object PlaylistWithTracksMapper {

    fun map(playlistWithTracks: PlaylistWithTracks): PlaylistShow {
        return PlaylistShow(
            playlist = Playlist(
                playlistId = playlistWithTracks.playlist.playlistId,
                playlistName = playlistWithTracks.playlist.playlistName,
                playlistDescription = playlistWithTracks.playlist.playlistDescription,
                playlistCoverUri = playlistWithTracks.playlist.playlistCoverUri,
                countTracks = playlistWithTracks.tracks.size
            ),
            tracks = playlistWithTracks.tracks.map { trackEntity ->
                TrackDbMapper.map(trackEntity)
            }
        )
    }
}