package com.example.playlistmaker.search.data.network

import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesAPI,
    private val capabilities: NetworkCapabilities?,
) : NetworkClient {

    override suspend fun searchTrackRequest(dto: Any): TrackResponse {
        if (!isConnected()) {
            return TrackResponse().apply { resultCode = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return TrackResponse().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesApiService.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                TrackResponse().apply{ resultCode = 500 }
            }
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