package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.fragments.PlayerFragment
import com.example.playlistmaker.search.ui.model.TrackUi
import com.example.playlistmaker.search.ui.recycler.TrackAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.TrackSearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var searchText: String = ""
    private lateinit var binding: FragmentSearchBinding
    private var isClickAllowed = true
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var searchTextWatcher: TextWatcher

    private val tracksAdapter = TrackAdapter {
        if (isClickAllowed) {
            val playerActivityIntent = Intent(requireContext(), PlayerFragment::class.java)
            playerActivityIntent.putExtra(MediaStore.Audio.AudioColumns.TRACK, it)
            startActivity(playerActivityIntent)
            viewModel.onItemClick(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //адаптеры списков
        binding.trackListView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackListView.adapter = tracksAdapter

        // Обработчик нажатия стрелки НАЗАД
        binding.backToMain.setOnClickListener {
            requireActivity().finish()
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
                viewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchField.addTextChangedListener(searchTextWatcher)

        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.isClickAllowedLiveData.observe(viewLifecycleOwner) {
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

    private fun render(state: TrackSearchState) {
        when (state) {
            TrackSearchState.Loading -> progressBarPlaceholder()
            is TrackSearchState.History -> historyPlaceholder(state.tracks)
            is TrackSearchState.Content -> showSearchedTrackList(state.tracks)
            is TrackSearchState.Empty -> emptySearchPlaceholder(state.message)
            is TrackSearchState.Error -> errorPlaceholder(state.errorMessage)
        }
    }

    // Обработчик отображения истории поиска
    private fun historyPlaceholder(historyTracks: List<TrackUi>){
        binding.progressBar.visibility = View.GONE
        binding.historyHeaderText.visibility = View.VISIBLE
        binding.historyTrackListView.visibility = View.VISIBLE
        binding.historyClearButton.visibility = View.VISIBLE

        tracksAdapter.tracks.clear()
        tracksAdapter.tracks.addAll(historyTracks)
        tracksAdapter.notifyDataSetChanged()
    }

    // Обработчик показа найденных треков
    private fun showSearchedTrackList(tracks: List<TrackUi>){
        binding.progressBar.visibility = View.GONE
        binding.historyHeaderText.visibility = View.GONE
        binding.historyTrackListView.visibility = View.GONE
        binding.historyClearButton.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
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
    }

    // Обработчик отображения плейсхолдера прогресс бара
    private fun progressBarPlaceholder() {
        println("Loading")
        binding.progressBar.visibility = View.VISIBLE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.historyHeaderText.visibility = View.GONE
        binding.historyTrackListView.visibility = View.GONE
        binding.historyClearButton.visibility = View.GONE
        binding.trackListView.visibility = View.GONE
    }

    // Обработка появления/исчезновения крестика очистки поля ввода
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearButtonOnClick() {
        binding.searchField.clearFocus()
        binding.searchField.setText("")

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchField.windowToken, 0)

        viewModel.onClearButtonClick()
    }
}