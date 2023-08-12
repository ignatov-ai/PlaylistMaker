package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView
    private lateinit var btnBackToMain: ImageView

    private lateinit var searchPlaceholder: FrameLayout
    private lateinit var searchPlaceholderErrorIcon: ImageView
    private lateinit var searchPlaceholderErrorText: TextView
    private lateinit var searchPlaceholderRefreshButton: Button

    var searchText: String? = null

    private lateinit var trackListView: RecyclerView

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Добавление треков в список
    val tracks: MutableList<Track> = mutableListOf()

    // Список с треками истории
    val historyTracks: MutableList<String> = mutableListOf()
    val HISTORY_TRACK_COUNT = 3

    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private var tracksAdapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchPlaceholder = findViewById(R.id.searchPlaceholder)
        searchPlaceholderErrorIcon = findViewById(R.id.searchPlaceholderErrorIcon)
        searchPlaceholderErrorText = findViewById(R.id.searchPlaceholderErrorText)
        searchPlaceholderRefreshButton = findViewById(R.id.searchPlaceholderRefreshButton)

        trackListView = findViewById(R.id.trackListView)
        trackListView.layoutManager = LinearLayoutManager(this)

        trackListView.adapter = tracksAdapter

        tracksAdapter.setOnItemClickListener(object : TrackAdapter.OnListElementClickListener {
            override fun onListElementClick(position: Int) {
                searchHistory.addToHistory(tracksAdapter.getTrack(position))
            }
        })

        btnBackToMain = findViewById(R.id.backToMain)
        btnBackToMain.setOnClickListener {
            finish()
        }

        searchField = findViewById(R.id.searchField)
        clearButton = findViewById(R.id.searchClearBar)

        clearButton.setOnClickListener {
            searchField.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchField.addTextChangedListener(searchTextWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }

        searchPlaceholderRefreshButton.setOnClickListener {
            searchTrack()
            hidePlaceholder()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchField.setText(searchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchTrack() {
        if (searchText.toString().isNotEmpty()) {
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()

            iTunesService.search(searchText.toString()).enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>)
                    {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)

                            historyListAdd(searchText.toString())

                            Log.d("history_list", historyTracks.toString())
                            hidePlaceholder()
                            tracksAdapter.notifyDataSetChanged()
                        } else {
                            if (tracks.isEmpty()) {
                                emptySearchPlaceholder()
                            } else {
                                errorPlaceholder()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    errorPlaceholder()
                }
            })
        }
    }

    private fun historyListAdd(id: String){
        if (!historyTracks.contains(id)) {
            historyTracks.add(0, id)
            if (historyTracks.size > HISTORY_TRACK_COUNT){
                historyTracks.removeAt(HISTORY_TRACK_COUNT)
            }
        }
        else{
            historyTracks.remove(id)
            historyTracks.add(0, id)
            if (historyTracks.size > HISTORY_TRACK_COUNT){
                historyTracks.removeAt(HISTORY_TRACK_COUNT)
            }
        }
    }
    private fun hidePlaceholder(){
        searchPlaceholder.visibility = View.GONE
        searchPlaceholderErrorIcon.visibility = View.GONE
        searchPlaceholderErrorText.visibility = View.GONE
        searchPlaceholderRefreshButton.visibility = View.GONE
    }

    private fun emptySearchPlaceholder(){
        searchPlaceholder.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.visibility = View.VISIBLE
        searchPlaceholderErrorText.visibility = View.VISIBLE
        searchPlaceholderRefreshButton.visibility = View.GONE
        searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_tracks)
        searchPlaceholderErrorText.text = getString(R.string.nothingWasFound)
        tracksAdapter.notifyDataSetChanged()
    }

    private fun errorPlaceholder(){
        searchPlaceholder.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.visibility = View.VISIBLE
        searchPlaceholderErrorText.visibility = View.VISIBLE
        searchPlaceholderRefreshButton.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_internet)
        searchPlaceholderErrorText.text = getString(R.string.noInternet)
        tracksAdapter.notifyDataSetChanged()
    }
}