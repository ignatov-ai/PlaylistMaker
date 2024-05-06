package com.example.playlistmaker.favourites.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favourites.domain.FavouritesInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackUiMapper
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favouritesInteractor: FavouritesInteractor
    ): ViewModel() {
    private val favouriteLiveData = MutableLiveData<FavouriteState>()
    val favouritesListOfLiveData: LiveData<FavouriteState> = favouriteLiveData

    private var isClickAllowed = true

    fun onViewCreatedOnScreen() {
        viewModelScope.launch {
            favouritesInteractor
                .getTracksFromFavourites()
                .collect { tracks ->
                    handleResult(tracks)
                }
        }
    }

    private fun handleResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            setState(FavouriteState.Empty)
        } else {
            val tracksUi = tracks.map { item ->
                TrackUiMapper.map(item)
            }
            setState(FavouriteState.Content(tracks = tracksUi))
        }
    }

    private fun setState(state: FavouriteState) {
        favouriteLiveData.postValue(state)
    }
}