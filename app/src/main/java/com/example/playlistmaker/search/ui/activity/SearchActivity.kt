package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.old.TrackSearchResponse
import com.example.playlistmaker.data.old.ITunesAPI
import com.example.playlistmaker.search.ui.model.Track
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.ui.old.HISTORY_PREFS
import com.example.playlistmaker.ui.old.SearchTrackHistory
import com.example.playlistmaker.search.ui.recycler.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), TrackAdapter.RecycleViewListener {
    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    var searchText: String? = null

    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView
    private lateinit var btnBackToMain: ImageView

    private lateinit var historyHeaderText: TextView
    private lateinit var historyTrackListView: RecyclerView
    private lateinit var historyClearButton: Button

    private lateinit var trackListView: RecyclerView

    private lateinit var searchPlaceholder: FrameLayout
    private lateinit var searchPlaceholderErrorIcon: ImageView
    private lateinit var searchPlaceholderErrorText: TextView
    private lateinit var searchPlaceholderRefreshButton: Button

    private lateinit var progressBar: ProgressBar

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)

    // Основной список треков
    private val tracks: MutableList<Track> = mutableListOf()
    private val tracksAdapter = TrackAdapter(tracks,this)

    private val searchRunnable = Runnable { searchTrackRequest() }

    // Список треков истории
    val historyTracks: MutableList<Track> = mutableListOf()
    private var historyAdapter = TrackAdapter(historyTracks,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        btnBackToMain = findViewById(R.id.backToMain)
        searchField = findViewById(R.id.searchField)
        clearButton = findViewById(R.id.searchClearBar)

        searchPlaceholder = findViewById(R.id.searchPlaceholder)
        searchPlaceholderErrorIcon = findViewById(R.id.searchPlaceholderErrorIcon)
        searchPlaceholderErrorText = findViewById(R.id.searchPlaceholderErrorText)
        searchPlaceholderRefreshButton = findViewById(R.id.searchPlaceholderRefreshButton)

        progressBar = findViewById(R.id.progressBar)

        trackListView = findViewById(R.id.trackListView)

        historyHeaderText = findViewById(R.id.historyHeaderText)
        historyTrackListView = findViewById(R.id.historyTrackListView)
        historyClearButton = findViewById(R.id.historyClearButton)

        //адаптеры списков
        trackListView.layoutManager = LinearLayoutManager(this)
        trackListView.adapter = tracksAdapter

        historyTrackListView.layoutManager = LinearLayoutManager(this)
        historyTrackListView.adapter = historyAdapter

        // Обработчик нажатия стрелки НАЗАД
        btnBackToMain.setOnClickListener {
            finish()
        }

        // Обработчик нажатия на КРЕСТИК ОЧИСТКИ ПОЛЯ ВВОДА
        clearButton.setOnClickListener {
            searchField.clearFocus()
            searchField.setText("")
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()

            if (historySize() > 0) {
                historyPlaceholder()
            }else{
                hidePlaceholder()
            }

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        // Отслеживание изменений поля ввода
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchField.addTextChangedListener(searchTextWatcher)

        // Проверяем фокус в поле ввода и убираем историю поиска, если поставим курсор в поле ввода
        searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hidePlaceholder()
            }
        }

        // Получаем список треков из истории при загрузке экрана
        if (historySize()>0) {
            historyPlaceholder()
        }else{
            hidePlaceholder()
        }

        // Обработчик нажатия кнопки ОЧИСТИТЬ ИСТОРИЮ
        historyClearButton.setOnClickListener {
            var sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
            SearchTrackHistory(sharedPrefs).clearHistory()
            historyTracks.clear()
            historyAdapter.notifyDataSetChanged()
            hidePlaceholder()

        }

        // Обработчик нажатия кнопки ОБНОВИТЬ при отсутствии сети
        searchPlaceholderRefreshButton.setOnClickListener {
            searchTrackRequest()
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

    // Обработка появления/исчезновения крестика очистки поля ввода
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // Обработчик поиска трека на сервисе iTunesAPI
    private fun searchTrackRequest() {
        if (searchText.toString().isNotEmpty()) {
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            searchPlaceholder.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            iTunesService.search(searchText.toString()).enqueue(object :
                Callback<TrackSearchResponse> {
                override fun onResponse(call: Call<TrackSearchResponse>,
                                        response: Response<TrackSearchResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackListView.visibility = View.VISIBLE

                            /*
                            исправил ошибку несовпадения типов, т.к. в уроке с imdb была строка
                            movies.addAll(response.body()?.results!!)
                            дающая ошибку несовпадения типов.
                            другого сполсоба пока не придумал, прошу помочь, если есть способ проще
                            */
                            val trackList = mutableListOf<Track>()
                            for (trackDto in response.body()?.results!!) {
                                val track = Track(
                                    trackDto.trackId,
                                    trackDto.trackName,
                                    trackDto.artistName,
                                    trackDto.trackTimeMillis,
                                    trackDto.artworkUrl100,
                                    trackDto.collectionName,
                                    trackDto.releaseDate,
                                    trackDto.primaryGenreName,
                                    trackDto.country,
                                    trackDto.previewUrl)
                                trackList.add(track)
                            }
                            tracks.addAll(trackList)
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            emptySearchPlaceholder()
                        } else {
                            hidePlaceholder()
                        }
                    } else {
                        errorPlaceholder()
                    }
                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    errorPlaceholder()
                }
            })
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    // Обработчик скрытия плейсхолдера
    private fun hidePlaceholder(){
        progressBar.visibility = View.GONE
        historyHeaderText.visibility = View.GONE
        historyTrackListView.visibility = View.GONE
        historyClearButton.visibility = View.GONE

        searchPlaceholder.visibility = View.GONE
        searchPlaceholderErrorIcon.visibility = View.GONE
        searchPlaceholderErrorText.visibility = View.GONE
        searchPlaceholderRefreshButton.visibility = View.GONE
    }

    // Обработчик отображения плейсхолдера ПУСТОГО СПИСКА
    private fun emptySearchPlaceholder(){
        progressBar.visibility = View.GONE
        searchPlaceholder.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.visibility = View.VISIBLE
        searchPlaceholderErrorText.visibility = View.VISIBLE
        searchPlaceholderRefreshButton.visibility = View.GONE
        searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_tracks)
        searchPlaceholderErrorText.text = getString(R.string.nothingWasFound)
        tracksAdapter.notifyDataSetChanged()
    }

    // Обработчик отображения плейсхолдера ОТСУТСТВИЯ СЕТИ
    private fun errorPlaceholder(){
        progressBar.visibility = View.GONE
        searchPlaceholder.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.visibility = View.VISIBLE
        searchPlaceholderErrorText.visibility = View.VISIBLE
        searchPlaceholderRefreshButton.visibility = View.VISIBLE
        searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_internet)
        searchPlaceholderErrorText.text = getString(R.string.noInternet)
        tracksAdapter.notifyDataSetChanged()
    }

    private fun historySize(): Int{
        var sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
        historyTracks.clear()
        historyTracks.addAll(SearchTrackHistory(sharedPrefs).getHistoryList()!!.toMutableList())
        return historyTracks.size
    }
    private fun historyPlaceholder(){
        var sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
        historyTracks.clear()
        historyTracks.addAll(SearchTrackHistory(sharedPrefs).getHistoryList()!!.toMutableList())
        historyAdapter.notifyDataSetChanged()
        historyHeaderText.visibility = View.VISIBLE
        historyTrackListView.visibility = View.VISIBLE
        historyClearButton.visibility = View.VISIBLE
    }

    override fun onItemClick(track: Track) {
        var sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
        SearchTrackHistory(sharedPrefs).historyAddTrack(track)
        historyAdapter.notifyDataSetChanged()

        // передаем данные на следующий экран
        val bundle = Bundle().apply{
            putString("trackImage", track.artworkUrl512)
            putString("trackName", track.trackName)
            putString("artistName", track.artistName)
            putString("country", track.country)
            putString("trackTimeMills", track.timeToMins)
            putString("collectionName",track.collectionName)
            putString("releaseDate",track.releaseYear)
            putString("primaryGenreName",track.primaryGenreName)
            putString("previewUrl",track.previewUrl)
        }

        if (clickDebounce()) {
            val playerIntent = Intent(this@SearchActivity, PlayerActivity::class.java).apply {
                putExtras(bundle)
            }
            startActivity(playerIntent)
        }
    }
}