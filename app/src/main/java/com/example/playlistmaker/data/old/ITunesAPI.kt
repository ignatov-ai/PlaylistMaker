package com.example.playlistmaker.data.old

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TrackSearchResponse>
}