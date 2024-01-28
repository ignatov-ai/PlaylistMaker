package com.example.playlistmaker.favourites.domain.impl

import com.example.playlistmaker.favourites.domain.FavouritesInteractor
import com.example.playlistmaker.favourites.domain.FavouritesRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesInteractorImpl(
    private val favouritesRepository: FavouritesRepository
    ): FavouritesInteractor {
    override suspend fun addTrackToFavourites(track: Track) {
        favouritesRepository.addTrackToFavourites(track)
    }

    override suspend fun deleteTrackFromFavourites(track: Track) {
        favouritesRepository.deleteTrackFromFavourites(track)
    }

    override fun getTracksFromFavourites(): Flow<List<Track>> {
        return favouritesRepository.getTracksFromFavourites().map { tracks -> tracks.reversed() }
    }
}