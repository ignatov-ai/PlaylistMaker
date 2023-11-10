package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.ui.model.TrackUi

sealed interface TrackSearchState {
    object Loading : TrackSearchState

    data class Content(
        val tracks: List<TrackUi>
    ) : TrackSearchState

    data class Error(
        val errorMessage: String
    ) : TrackSearchState

    data class Empty(
        val message: String
    ) : TrackSearchState

    data class History(
        val tracks: List<TrackUi>
    ) : TrackSearchState
}