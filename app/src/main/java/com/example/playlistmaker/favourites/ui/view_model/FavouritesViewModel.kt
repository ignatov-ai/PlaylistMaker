package com.example.playlistmaker.favourites.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.ui.model.TrackUi

class FavouritesViewModel: ViewModel() {
    private val favouritesLiveData = MutableLiveData<List<TrackUi>>()
    val listOfFavouritesLiveData = favouritesLiveData

    init {
        favouritesLiveData.postValue(emptyList())
    }
}