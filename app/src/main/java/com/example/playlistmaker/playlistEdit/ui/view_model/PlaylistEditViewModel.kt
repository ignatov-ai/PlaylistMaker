package com.example.playlistmaker.playlistEdit.ui.view_model

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.newplaylist.domain.NewPlaylistInteractor
import com.example.playlistmaker.newplaylist.ui.view_model.NewPlaylistViewModel
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlistEdit.domain.api.PlaylistEditInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    newPlaylistInteractor: NewPlaylistInteractor,
    private val playlistEditInteractor: PlaylistEditInteractor,
) : NewPlaylistViewModel(newPlaylistInteractor) {

    override fun onButtonSaveClick(
        playlistId: Long?,
        playlistName: String,
        playlistDescription: String,
        playlistCoverUri: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistEditInteractor.playlistUpdate(
                Playlist(
                    playlistId = playlistId,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    playlistCoverUri = playlistCoverUri
                )
            )
        }
    }
}