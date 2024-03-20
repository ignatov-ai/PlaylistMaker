package com.example.playlistmaker.newplaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity

@Dao
interface PlaylistDao {

    @Query("SELECT count(*) FROM playlist_track_table WHERE playlistId = :playlistId")
    fun countOfTracksInPlaylist(playlistId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): List<PlaylistEntity>
}