package com.example.playlistmaker.newplaylist.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.newplaylist.domain.NewPlaylistInteractor
import com.example.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val newPlaylistInteractor: NewPlaylistInteractor) :
    ViewModel() {

    open fun onButtonSaveClick(
        playlistId: Long?,
        playlistName: String,
        playlistDescription: String,
        playlistCoverUri: String,
    ) {
        val playlist = Playlist(
            playlistId = playlistId,
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            playlistCoverUri = playlistCoverUri
        )
        viewModelScope.launch(Dispatchers.IO) {
            newPlaylistInteractor.createPlaylist(playlist)
        }
    }

    fun mediaPicked(uri: Uri, imageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newPlaylistInteractor.saveImageToStorage(uri.toString(), imageId)
        }
    }
}