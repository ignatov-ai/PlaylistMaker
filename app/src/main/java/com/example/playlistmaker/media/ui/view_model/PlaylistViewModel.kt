package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.ui.model.TrackUi

class PlaylistViewModel: ViewModel() {
    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    val listOfPlaylistsLiveData = playlistsLiveData

    init {
        playlistsLiveData.postValue(emptyList())
    }
}