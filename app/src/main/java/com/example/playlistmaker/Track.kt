package com.example.playlistmaker

data class Track(
    var trackId: String,
    var trackName: String,
    var artistName: String,
    var trackTimeMillis: String,
    var artworkUrl100: String,
    var collectionName: String,
    var releaseDate: String,
    var primaryGenreName: String,
    var country: String
)