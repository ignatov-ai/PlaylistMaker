package com.example.playlistmaker.newplaylist.data.db.dao

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.playlistmaker.favourites.data.db.TrackEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistTrackEntity

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistTrackEntity::class)
    )
    val tracks: List<TrackEntity>
)
