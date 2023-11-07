package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.ui.model.TrackUi
import com.example.playlistmaker.search.ui.recycler.TrackAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.TrackSearchState

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var searchText: String = ""
    private lateinit var binding: ActivitySearchBinding
    private var isClickAllowed = true
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchTextWatcher: TextWatcher

    private val tracksAdapter = TrackAdapter {
        if (isClickAllowed) {
            val playerActivityIntent = Intent(this, PlayerActivity::class.java)
            playerActivityIntent.putExtra(TRACK, it)
            startActivity(playerActivityIntent)
            viewModel.onItemClick(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]

        //адаптеры списков
        binding.trackListView.layoutManager = LinearLayoutManager(this)
        binding.trackListView.adapter = tracksAdapter

        // Обработчик нажатия стрелки НАЗАД
        binding.backToMain.setOnClickListener {
            finish()
        }

        // Обработчик нажатия на КРЕСТИК ОЧИСТКИ ПОЛЯ ВВОДА
        binding.searchClearBar.setOnClickListener {
            clearButtonOnClick()
        }

        // Отслеживание изменений поля ввода
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearBar.visibility = clearButtonVisibility(s)
                searchText = s.toString()
                viewModel.searchDebounce(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchField.addTextChangedListener(searchTextWatcher)

        viewModel.observeStateLiveData().observe(this) {
            render(it)
        }
        viewModel.isClickAllowedLiveData.observe(this) {
            isClickAllowed = it
        }

        // Обработчик нажатия кнопки ОБНОВИТЬ при отсутствии сети
        binding.searchPlaceholderRefreshButton.setOnClickListener {
            if (isClickAllowed) {
                viewModel.searchWithoutDebounce(searchText)
            }
        }

        // Обработчик нажатия кнопки ОЧИСТИТЬ ИСТОРИЮ
        binding.historyClearButton.setOnClickListener {
            viewModel.onClearHistoryClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchField.removeTextChangedListener(searchTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        binding.searchField.setText(searchText)
    }

    private fun clearButtonOnClick() {
        binding.searchField.clearFocus()
        binding.searchField.setText("")

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchField.windowToken, 0)

        viewModel.onClearButtonClick()
    }

    private fun render(state: TrackSearchState) {
        when (state) {
            is TrackSearchState.History -> historyPlaceholder(state.tracks)
            is TrackSearchState.Content -> hidePlaceholder(state.tracks)
            is TrackSearchState.Empty -> emptySearchPlaceholder(state.message)
            is TrackSearchState.Error -> errorPlaceholder(state.errorMessage)
            TrackSearchState.Loading -> progressBarPlaceholder()
        }
    }

    // Обработчик отображения истории поиска
    private fun historyPlaceholder(historyTracks: List<TrackUi>){
        binding.historyHeaderText.visibility = View.VISIBLE
        binding.historyTrackListView.visibility = View.VISIBLE
        binding.historyClearButton.visibility = View.VISIBLE

        tracksAdapter.tracks.clear()
        tracksAdapter.tracks.addAll(historyTracks)
        tracksAdapter.notifyDataSetChanged()
    }

    // Обработчик скрытия плейсхолдера
    private fun hidePlaceholder(tracks: List<TrackUi>){
        binding.progressBar.visibility = View.GONE
        binding.historyHeaderText.visibility = View.GONE
        binding.historyTrackListView.visibility = View.GONE
        binding.historyClearButton.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
        binding.searchPlaceholderErrorIcon.visibility = View.GONE
        binding.searchPlaceholderErrorText.visibility = View.GONE
        binding.searchPlaceholderRefreshButton.visibility = View.GONE
        binding.trackListView.visibility = View.VISIBLE

        tracksAdapter.tracks.clear()
        tracksAdapter.tracks.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()
    }

    // Обработчик отображения плейсхолдера ПУСТОГО СПИСКА
    private fun emptySearchPlaceholder(message: String){
        binding.progressBar.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.searchPlaceholderErrorIcon.visibility = View.VISIBLE
        binding.searchPlaceholderErrorText.visibility = View.VISIBLE
        binding.searchPlaceholderRefreshButton.visibility = View.GONE
        binding.searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_tracks)
        binding.searchPlaceholderErrorText.text = getString(R.string.nothingWasFound)

        tracksAdapter.notifyDataSetChanged()
    }

    // Обработчик отображения плейсхолдера ОТСУТСТВИЯ СЕТИ
    private fun errorPlaceholder(errorMessage: String){
        binding.progressBar.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.searchPlaceholderErrorIcon.visibility = View.VISIBLE
        binding.searchPlaceholderErrorText.visibility = View.VISIBLE
        binding.searchPlaceholderRefreshButton.visibility = View.VISIBLE
        binding.searchPlaceholderErrorIcon.setImageResource(R.drawable.error_no_internet)
        binding.searchPlaceholderErrorText.text = getString(R.string.noInternet)

        tracksAdapter.notifyDataSetChanged()
    }

    // Обработчик отображения плейсхолдера прогресс бара
    private fun progressBarPlaceholder() {
        binding.searchPlaceholder.visibility = View.GONE
        binding.historyHeaderText.visibility = View.GONE
        binding.historyTrackListView.visibility = View.GONE
        binding.searchPlaceholderErrorIcon.visibility = View.GONE
        binding.searchPlaceholderErrorText.visibility = View.GONE
        binding.searchPlaceholderRefreshButton.visibility = View.GONE
        binding.trackListView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }



    // Обработка появления/исчезновения крестика очистки поля ввода
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}