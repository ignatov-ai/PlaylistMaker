package com.example.playlistmaker.playlistShow.data.db

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.newplaylist.data.db.dao.PlaylistWithTracks
import com.example.playlistmaker.newplaylist.data.mapper.PlaylistWithTracksMapper
import com.example.playlistmaker.playlistShow.domain.api.PlaylistShowRepository
import com.example.playlistmaker.playlistShow.domain.model.PlaylistShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map


class PlaylistShowRepositoryImpl (private val database: AppDatabase) : PlaylistShowRepository {
    override fun getPlaylistShow(playlistId: Long): Flow<PlaylistShow> {
        return database.playlistShowDao()
            .getPlaylist(playlistId)
            .flatMapMerge { playlist ->
                database.playlistShowDao().getTracksFromPlaylist(playlistId)
                    .map { tracks ->
                        PlaylistWithTracksMapper.map(
                            PlaylistWithTracks(
                                playlist = playlist,
                                tracks = tracks
                            )
                        )
                    }
            }
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        database.playlistShowDao().removeTrackFromPlaylist(playlistId, trackId)
        if (!database.playlistShowDao().isTrackInPlaylists(trackId) &&
            !database.playlistShowDao().isTrackIsFavorite(trackId)
        ) {
            database.playlistShowDao().removeTrackFromDb(trackId)
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        database.playlistShowDao().deletePlaylist(playlistId)
    }
}