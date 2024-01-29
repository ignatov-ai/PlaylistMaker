package com.example.playlistmaker.search.data.storage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackStorage(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Parcelable