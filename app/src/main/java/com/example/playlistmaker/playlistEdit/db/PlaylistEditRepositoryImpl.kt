package com.example.playlistmaker.playlistEdit.db

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.newplaylist.data.mapper.PlaylistEntityMapper
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlistEdit.domain.api.PlaylistEditRepository

class PlaylistEditRepositoryImpl(private val database: AppDatabase) : PlaylistEditRepository {
    override suspend fun playlistUpdate(playlist: Playlist) {
        database.playlistEditDao().updatePlaylist(PlaylistEntityMapper.map(playlist))
    }
}