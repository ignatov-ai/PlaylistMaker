package com.example.playlistmaker.favourites.data

import com.example.playlistmaker.favourites.data.db.AppDatabase
import com.example.playlistmaker.favourites.data.mapper.TrackDbMapper
import com.example.playlistmaker.favourites.domain.FavouritesRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl (
    private val database: AppDatabase,
) : FavouritesRepository {

    override suspend fun addTrackToFavourites(track: Track) {
        database.trackDao().addTrack(TrackDbMapper.map(track))
    }

    override suspend fun deleteTrackFromFavourites(track: Track) {
        database.trackDao().removeTrack(TrackDbMapper.map(track))
    }

    override fun getTracksFromFavourites(): Flow<List<Track>> = flow {
        val tracks = database.trackDao().getTracks()
        emit(tracks.map { track -> TrackDbMapper.map(track) })
    }
}