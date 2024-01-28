package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.mapper.TrackUiToDomain
import com.example.playlistmaker.search.ui.model.TrackUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TracksInteractor,
    private val trackHistoryInteractor: TracksHistoryInteractor,
) : ViewModel() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 750L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchText: String? = null
    private var isClickAllowed = true

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeStateLiveData(): LiveData<TrackSearchState> = stateLiveData

    private val mutableIsClickAllowedLiveData = MutableLiveData<Boolean>()
    val isClickAllowedLiveData: LiveData<Boolean> = mutableIsClickAllowedLiveData

    init {
        renderHistoryCheck()
    }

    fun searchDebounce(lastText: String) {
        if (searchText == lastText) {
            return
        }

        searchText = lastText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrackRequest(lastText)
        }
    }

    fun searchWithoutDebounce(changedText: String) {
        searchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchTrackRequest(changedText)
        }
    }

    private fun searchTrackRequest(newLastText: String) {
        if (newLastText.isNotEmpty()) {
            renderState(TrackSearchState.Loading)
            viewModelScope.launch {
                trackInteractor
                    .searchTracks(newLastText)
                    .collect{pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<TrackUi>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks.map { TrackToTrackUi().map(it) })
        }

        when {
            errorMessage != null -> {
                renderState(
                    TrackSearchState.Error(
                        errorMessage =  R.string.noInternet.toString())
                )

            }

            tracks.isEmpty() -> {
                renderState(
                    TrackSearchState.Empty(
                        message = R.string.nothingWasFound.toString())
                )
            }

            else -> {
                renderState(
                    TrackSearchState.Content(
                        tracks = tracks
                    )
                )
            }
        }
    }

    private fun renderState(state: TrackSearchState) {
        stateLiveData.postValue(state)
    }

    private fun renderHistoryCheck() {
        val list = trackHistory()
        if (list.isEmpty()) {
            renderState(TrackSearchState.Content(emptyList()))
        } else {
            renderState(TrackSearchState.History(list))
        }
    }

    private fun clickDebounce(){
        if (isClickAllowed) {
            isClickAllowed = false
            mutableIsClickAllowedLiveData.postValue(false)
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
                mutableIsClickAllowedLiveData.postValue(true)
            }
        }
    }

    private fun trackHistory(): MutableList<TrackUi> {
        return trackHistoryInteractor.getHistory().map { TrackToTrackUi().map(it) } as MutableList<TrackUi>
    }

    private fun saveHistory() {
        trackHistoryInteractor.saveHistory()
    }

    fun onItemClick(track: TrackUi) {
        clickDebounce()
        trackHistoryInteractor.addTrack(TrackUiToDomain().map(track))
        saveHistory()
        if (stateLiveData.value is TrackSearchState.History) {
            renderState(TrackSearchState.History(trackHistory()))
        }
    }

    fun onClearHistoryClick() {
        trackHistoryInteractor.clearHistory()
        renderState(TrackSearchState.Content(emptyList()))
    }

    fun onClearButtonClick() {
        renderHistoryCheck()
    }
}