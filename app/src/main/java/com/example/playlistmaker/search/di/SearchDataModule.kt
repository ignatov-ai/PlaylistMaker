package com.example.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.search.data.HistoryStorage
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ITunesAPI
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.storage.HISTORY_PREFS
import com.example.playlistmaker.search.data.storage.SharedPrefsHistory
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory<Gson> {
        Gson()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            HISTORY_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    single<HistoryStorage> {
        SharedPrefsHistory(sharedPreferences = get(), gson = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(iTunesApiService = get(), capabilities = get())
    }

    single<NetworkCapabilities?> {
        val connectivityManager = androidContext().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    }


}