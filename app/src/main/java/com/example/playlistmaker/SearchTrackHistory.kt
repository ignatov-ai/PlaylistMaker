package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_PREFS = "Playlist_Maker_History"
const val HISTORY_KEY = "history_key"

class SearchTrackHistory: AppCompatActivity() {

    val sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
    fun addHistory(trackHistoryList: MutableList<String>){
        val json = Gson()
        val historyToString = json.toJson(trackHistoryList)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, historyToString)
            .apply()
    }

    fun getHistory(): ArrayList<Track>{
        val jsonFromString = sharedPrefs.getString(HISTORY_KEY, "")

        val json = Gson()
        val trackListType = object : TypeToken<List<Track>>() {}.type
        val trackHistoryList: ArrayList<Track> = json.fromJson(jsonFromString, trackListType)

        return trackHistoryList
    }

    fun clearHistory(){
        sharedPrefs.edit()
            .remove(HISTORY_KEY)
            .apply()
    }
}