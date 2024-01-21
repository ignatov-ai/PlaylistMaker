package com.example.playlistmaker.favourites.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.favourites.data.db.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackEntity)

    @Delete
    suspend fun removeTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks_table")
    suspend fun getIdTracks(): List<Long>

}