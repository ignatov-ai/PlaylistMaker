package com.example.playlistmaker.favourites.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    suspend fun addTrackToFavourites(track: Track)
    suspend fun deleteTrackFromFavourites(track: Track)
    fun getTracksFromFavourites(): Flow<List<Track>>
}