package com.example.playlistmaker.search.domain.model

data class Track(
    val trackId: Long = 0,
    val trackName: String = "",
    val artistName: String = "",
    val trackTimeMillis: Long = 0,
    val artworkUrl100: String = "",
    val collectionName: String = "",
    val releaseDate: String = "",
    val primaryGenreName: String = "",
    val country: String = "",
    val previewUrl: String = "",
    var isFavourite: Boolean = false
)