package com.example.playlistmaker.newplaylist.data.db

import androidx.room.Entity

@Entity(
    tableName = "playlist_track_table",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [
        androidx.room.ForeignKey(
            onDelete = androidx.room.ForeignKey.Companion.CASCADE,
            entity = PlaylistEntity::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"]
        )
    ]
)
data class PlaylistTrackEntity(
    val playlistId: Long,
    val trackId: Long,
    val createTime: Long,
)