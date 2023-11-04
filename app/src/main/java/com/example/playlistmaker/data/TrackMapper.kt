package com.example.playlistmaker.data

import com.example.playlistmaker.domain.Track

class TrackMapper {
    fun mapToDomain(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }
}