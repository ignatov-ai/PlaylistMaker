package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.favourites.domain.model.Playlist

class PlaylistViewModel: ViewModel() {
    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    val listOfPlaylistsLiveData = playlistsLiveData

    init {
        playlistsLiveData.postValue(emptyList())
    }
}