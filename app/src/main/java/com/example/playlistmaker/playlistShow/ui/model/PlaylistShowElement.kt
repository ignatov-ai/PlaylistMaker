package com.example.playlistmaker.playlistShow.ui.model

import android.os.Parcelable
import com.example.playlistmaker.search.ui.model.TrackUi
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaylistShowElement(
    val playlistId: Long,
    var playlistName: String,
    var playlistDescription: String,
    var playlistCoverPath: String,
    var countTracks: Int,
    var playlistDuration: Long,
    var tracks: List<TrackUi>
) : Parcelable