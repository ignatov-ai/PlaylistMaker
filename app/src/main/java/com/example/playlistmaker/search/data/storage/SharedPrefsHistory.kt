package com.example.playlistmaker.search.data.storage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.search.data.HistoryStorage
import com.example.playlistmaker.search.data.storage.model.TrackStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_PREFS = "Playlist_Maker_History"
const val HISTORY_KEY = "history_key"

class SharedPrefsHistory(context: Context) : HistoryStorage {

    val sharedPreferences = context.getSharedPreferences(
        HISTORY_PREFS,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun getHistoryList(): List<TrackStorage> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        return if (json != null) {
            val myType = object : TypeToken<List<TrackStorage>>() {}.type
            Gson().fromJson(json, myType)
        } else {
            emptyList()
        }
    }

    override fun saveHistory(list: List<TrackStorage>) {
        val json = Gson().toJson(list)
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