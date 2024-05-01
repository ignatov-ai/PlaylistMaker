package com.example.playlistmaker.playlistEdit.db.dao

import androidx.room.Dao
import androidx.room.Update
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity

@Dao
interface PlaylistEditDao {

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
}