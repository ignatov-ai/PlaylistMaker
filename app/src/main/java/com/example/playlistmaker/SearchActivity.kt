package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

    private lateinit var searchPlaceholderErrorIcon: ImageView
    private lateinit var searchPlaceholderErrorText: TextView

    private var searchText: String? = null

    private lateinit var trackListView: RecyclerView

    private val iTunesBaseUrl = "https://imdb-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        trackListView = findViewById(R.id.trackList)
        trackListView.layoutManager = LinearLayoutManager(this)

        // Добавление треков в список
        val trackList: MutableList<Track> = mutableListOf()

        val trackAdapter = TrackAdapter(trackList)
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
                true
            }
            false
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

        searchPlaceholderErrorIcon = findViewById(R.id.searchPlaceholderErrorIcon)
        searchPlaceholderErrorText = findViewById(R.id.searchPlaceholderErrorText)
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
        if (searchField.text.isNotEmpty()) {
            iTunesService.findTrack(searchField.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>, response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                showMessage("Ничего не нашлось...", "")
                            } else {
                                showMessage("Результаты поиска:", "")
                            }
                        } else {
                            showMessage(
                                "Что-то пошло не так...",
                                response.code().toString()
                            )
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage("Что-то пошло не так...", "")
                    }
                })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            searchPlaceholderErrorText.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            searchPlaceholderErrorText.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            searchPlaceholderErrorText.visibility = View.GONE
        }
    }
}