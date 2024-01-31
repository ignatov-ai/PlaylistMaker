package com.example.playlistmaker.favourites.ui.view_model

import com.example.playlistmaker.search.ui.model.TrackUi

sealed interface FavouriteState {
    object Empty : FavouriteState
    data class Content(val tracks: List<TrackUi>) : FavouriteState
}