package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.entity.Track

class TrackResponse(val results: List<Track>) : Response()