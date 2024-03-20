package com.example.playlistmaker.playlist.data

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.newplaylist.data.mapper.PlaylistEntityMapper
import com.example.playlistmaker.playlist.domain.api.PlaylistsRepository
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(private val database: AppDatabase) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val list: List<Playlist> = database.playlistDao().getPlaylists().map { playlistEntity ->
            PlaylistEntityMapper.map(playlistEntity).apply {
                countTracks = database.playlistDao().countOfTracksInPlaylist(playlistId!!)
            }
        }
        emit(list)
    }
}