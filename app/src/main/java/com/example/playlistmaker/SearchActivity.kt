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
import android.widget.Toast
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

    private var searchText: String? = null

    private lateinit var trackListView: RecyclerView

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Добавление треков в список
    val tracks: MutableList<Track> = mutableListOf()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private val adapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchPlaceholder = findViewById(R.id.searchPlaceholder)
        searchPlaceholderErrorIcon = findViewById(R.id.searchPlaceholderErrorIcon)
        searchPlaceholderErrorText = findViewById(R.id.searchPlaceholderErrorText)
        searchPlaceholderRefreshButton = findViewById(R.id.searchPlaceholderRefreshButton)

        trackListView = findViewById(R.id.trackList)
        trackListView.layoutManager = LinearLayoutManager(this)

        val trackAdapter = TrackAdapter(tracks)
        trackListView.adapter = trackAdapter

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

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack(searchField.text.toString())
            }
            false
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchField.clearFocus()
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                //Log.d("searchText", searchText.toString())
                //отправка запроса после каждого измененного символа
                //searchTrack(searchText.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchField.addTextChangedListener(searchTextWatcher)
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

    private fun searchTrack(query: String) {
        val lastQuery = query
        tracks.clear()
        if (query.isNotEmpty()) {
            iTunesService.search(query).enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            // плейсхолдер с пустым поиском
                            emptySearchPlaceholder()
                        } else {
                            // убираем плейсхолдер
                            hidePlaceholder()
                        }
                    } else {
                        // плейсхолдер с ошибкой
                        errorPlaceholder()
                        searchPlaceholderRefreshButton.setOnClickListener{
                            hidePlaceholder()
                            searchTrack(lastQuery)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    errorPlaceholder()
                    searchPlaceholderRefreshButton.setOnClickListener{
                        hidePlaceholder()
                        searchTrack(lastQuery)
                    }

                }
            })
        }
    }

    private fun hidePlaceholder(){
        searchPlaceholder.visibility = View.GONE
        searchPlaceholderErrorIcon.visibility = View.GONE
        searchPlaceholderErrorText.visibility = View.GONE
        searchPlaceholderRefreshButton.visibility = View.GONE
        trackListView.visibility = View.VISIBLE
    }

    private fun emptySearchPlaceholder(){
        adapter.notifyDataSetChanged()
        trackListView.visibility = View.INVISIBLE
        searchPlaceholder.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.visibility = View.VISIBLE
        searchPlaceholderErrorText.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_tracks)
        searchPlaceholderErrorText.text = getString(R.string.nothingWasFound)
    }

    private fun errorPlaceholder(){
        adapter.notifyDataSetChanged()
        trackListView.visibility = View.INVISIBLE
        searchPlaceholder.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.visibility = View.VISIBLE
        searchPlaceholderErrorText.visibility = View.VISIBLE
        searchPlaceholderRefreshButton.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_internet)
        searchPlaceholderErrorText.text = getString(R.string.noInternet)
    }
}