package com.example.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.PlaylistsInteractor
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlist.ui.mapper.PlaylistUiMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor): ViewModel() {
    private val playlistsLiveData = MutableLiveData<PlaylistsState>()
    val listOfPlaylistsLiveData: LiveData<PlaylistsState> = playlistsLiveData

    fun setState(state: PlaylistsState) {
        playlistsLiveData.postValue(state)
    }

    fun viewCreated() {
        setState(PlaylistsState.Empty)
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect { playlists ->
                handleResult(playlists)
            }
        }
    }

    private fun handleResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            setState(PlaylistsState.Empty)
        } else {
            val playlistsUi = playlists.map { playlist ->
                PlaylistUiMapper.map(playlist)
            }
            setState(PlaylistsState.Content(playlistsUi))
        }
    }
}