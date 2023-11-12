package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesAPI,
    private val capabilities: NetworkCapabilities?,
) : NetworkClient {

    override fun searchTrackRequest(dto: Any): TrackResponse {
        if (!isConnected()) {
            return TrackResponse().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return TrackResponse().apply { resultCode = 400 }
        }
        val response: retrofit2.Response<TrackSearchResponse>
        return try {
            response = iTunesApiService.search(dto.expression).execute()
            val body = response.body()
            if (body != null) {
                body.apply { resultCode = response.code() }
            } else {
                TrackResponse().apply { resultCode = response.code() }
            }
        } catch (e: Exception) {
            TrackResponse()
        }
    }

    private fun isConnected(): Boolean {
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}