package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

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