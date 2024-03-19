package com.example.playlistmaker.newplaylist.data

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.newplaylist.data.mapper.PlaylistEntityMapper
import com.example.playlistmaker.newplaylist.domain.NewPlaylistRepository
import com.example.playlistmaker.playlist.domain.model.Playlist

class NewPlaylistRepositoryImpl (
    private val database: AppDatabase,
    private val imagesStorage: ImagesStorage
) : NewPlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        database.playlistDao().addPlaylist(playlist = PlaylistEntityMapper.map(playlist))
    }

    override suspend fun saveImageToPrivateStorage(path: String, imageId: String) {
        imagesStorage.saveImage(path, imageId)
    }
}