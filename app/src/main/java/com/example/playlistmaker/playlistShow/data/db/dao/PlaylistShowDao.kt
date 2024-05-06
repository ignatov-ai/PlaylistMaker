package com.example.playlistmaker.playlistShow.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.favourites.data.db.TrackEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity
import com.example.playlistmaker.newplaylist.data.db.dao.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistShowDao {
    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

    @Query("SELECT * FROM tracks_table JOIN playlist_track_table ON tracks_table.trackId = playlist_track_table.trackId WHERE playlist_track_table.playlistId = :playlistId ORDER BY playlist_track_table.createTime DESC")
    fun getTracksFromPlaylist(playlistId: Long): Flow<List<TrackEntity>>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getPlaylist(playlistId: Long): Flow<PlaylistEntity>

    @Query("DELETE FROM playlist_track_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    suspend fun removeTrackFromDb(trackId: Long)

    @Query("SELECT EXISTS(SELECT * FROM playlist_track_table WHERE trackId = :trackId)")
    suspend fun isTrackInPlaylists(trackId: Long): Boolean

    @Query("SELECT isFavourite FROM tracks_table WHERE trackId = :trackId")
    suspend fun isTrackIsFavorite(trackId: Long): Boolean

    @Query("DELETE FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)
}