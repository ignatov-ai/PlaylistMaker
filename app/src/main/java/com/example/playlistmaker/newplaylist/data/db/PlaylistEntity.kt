package com.example.playlistmaker.newplaylist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var playlistId: Long,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverUri: String,
)