package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val HISTORY_PREFS = "Playlist_Maker_History"
const val HISTORY_KEY = "history_key"

class SearchTrackHistory (var sharedPref: SharedPreferences) {

    companion object {
        private const val HISTORY_TRACK_COUNT = 10
    }

    fun getHistoryList(): Array<Track>? {
        val jsonHistory = sharedPref.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(jsonHistory, Array<Track>::class.java)
    }

    fun historyAddTrack(track: Track) {
        var historyTracks = getHistoryList()?: emptyArray()
        val historyTracksList = historyTracks.toMutableList()

        var replyIndex: Int = -1
        for(i in historyTracksList){
            if(i.trackId == track.trackId){
                replyIndex = historyTracksList.indexOf(i)
            }
        }

        if(replyIndex != -1){
            historyTracksList.moveToFirst(replyIndex)
        }

        if(replyIndex == -1) {
            historyTracksList.add(0, track)

            if (historyTracksList.size > Companion.HISTORY_TRACK_COUNT) {
                historyTracksList.removeAt(Companion.HISTORY_TRACK_COUNT)
            }
        }

        historyTracks = historyTracksList.toTypedArray()
        val jsonHistory = Gson().toJson(historyTracks)
        sharedPref.edit()
            .putString(HISTORY_KEY, jsonHistory)
            .apply()
        Log.d("row_select_json", jsonHistory.toString())
    }

    fun clearHistory(){
        sharedPref.edit()
            .remove(HISTORY_KEY)
            .apply()
        Log.d("row_select_json", "История поиска очищена")
    }

    fun <T> MutableList<T>.moveToFirst(index: Int) {
        if (index > 0 && index < size) {
            val item = removeAt(index)
            add(0, item)
        }
    }
}