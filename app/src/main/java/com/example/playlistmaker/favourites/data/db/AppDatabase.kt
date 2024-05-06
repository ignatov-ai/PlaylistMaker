package com.example.playlistmaker.favourites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.favourites.data.db.dao.TrackDao
import com.example.playlistmaker.newplaylist.data.db.PlaylistEntity
import com.example.playlistmaker.newplaylist.data.db.PlaylistTrackEntity
import com.example.playlistmaker.newplaylist.data.db.dao.PlaylistDao
import com.example.playlistmaker.player.data.db.dao.TrackInPlaylistDao
import com.example.playlistmaker.playlistEdit.db.dao.PlaylistEditDao
import com.example.playlistmaker.playlistShow.data.db.dao.PlaylistShowDao

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
    abstract fun playlistShowDao(): PlaylistShowDao
    abstract fun playlistEditDao(): PlaylistEditDao
}