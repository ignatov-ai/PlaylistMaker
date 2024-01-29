package com.example.playlistmaker.search.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class TrackUi(
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
    val artworkUrl512: String,
    var isFavourite: Boolean
) : Parcelable {

    val releaseYear: String?
        get() = releaseDate?.substring(0,4)

    val timeToMins: String?
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toInt())
}

