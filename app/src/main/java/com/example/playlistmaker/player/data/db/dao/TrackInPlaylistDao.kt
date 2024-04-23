package com.example.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.newplaylist.data.db.PlaylistTrackEntity

@Dao
interface TrackInPlaylistDao {
    @Query("SELECT EXISTS(SELECT * FROM tracks_table WHERE trackId=:idTrack)")
    suspend fun existsTrackInDb(idTrack: Long): Boolean

    @Query("SELECT EXISTS(SELECT * FROM playlist_track_table WHERE playlistId=:playlistId AND trackId=:trackId)")
    suspend fun existsTrackInPlaylist(playlistId: Long, trackId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackInPlaylist(playlistTrackEntity: PlaylistTrackEntity)
}