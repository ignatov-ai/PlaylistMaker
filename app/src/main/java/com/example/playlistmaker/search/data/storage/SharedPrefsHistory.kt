package com.example.playlistmaker.search.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.search.data.HistoryStorage
import com.example.playlistmaker.search.data.storage.model.TrackStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_PREFS = "Playlist_Maker_History"
const val HISTORY_KEY = "history_key"

class SharedPrefsHistory(private val sharedPreferences: SharedPreferences, private val gson: Gson) : HistoryStorage {

    override fun getHistoryList(): List<TrackStorage> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        return if (json != null) {
            val myType = object : TypeToken<List<TrackStorage>>() {}.type
            gson.fromJson(json, myType)
        } else {
            emptyList()
        }
    }

    override fun saveHistory(list: List<TrackStorage>) {
        val json = gson.toJson(list)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
    }
}