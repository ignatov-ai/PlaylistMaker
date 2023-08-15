package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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

class SearchActivity : AppCompatActivity(), TrackAdapter.RecycleViewListener {
    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

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

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)

    // Основной список треков
    val tracks: MutableList<Track> = mutableListOf()
    private var tracksAdapter = TrackAdapter(tracks,this)

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
        Log.d("row_select_flag",historySize().toString())

        // Обработчик нажатия кнопки ОЧИСТИТЬ ИСТОРИЮ
        historyClearButton.setOnClickListener {
            var sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
            SearchTrackHistory(sharedPrefs).clearHistory()
            historyTracks.clear()
            historyAdapter.notifyDataSetChanged()
            hidePlaceholder()

        }

        // Применение поискового запроса по нажатию галочки экранной клавиатуры
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }

        // Обработчик нажатия кнопки ОБНОВИТЬ при отсутствии сети
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

    // Обработка появления/исчезновения крестика очистки поля ввода
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // Обработчик поиска трека на сервисе iTunesAPI
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

    // Обработчик скрытия плейсхолдера
    private fun hidePlaceholder(){
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

        val playerIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
        startActivity(playerIntent)
    //Log.d("row_select_history", historyTracks.toString())
        //Toast.makeText(this, "Нажали на трек ${track.trackId} ${track.trackName} ${track.artistName}", Toast.LENGTH_SHORT).show()
    }
}