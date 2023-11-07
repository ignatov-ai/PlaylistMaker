package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.mapper.TrackUiToDomain
import com.example.playlistmaker.search.ui.model.TrackUi

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    var searchText: String? = null

    private val trackSearchInteractor =
        Creator.provideTrackInteractor(getApplication<Application>())
    private val trackHistoryInteractor =
        Creator.provideTracksHistoryInteractor(getApplication<Application>())
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeStateLiveData(): LiveData<TrackSearchState> = stateLiveData

    private val mutableIsClickAllowedLiveData = MutableLiveData<Boolean>()
    val isClickAllowedLiveData: LiveData<Boolean> = mutableIsClickAllowedLiveData
    private var isClickAllowed = true

    init {
        renderHistoryCheck()
    }

    fun searchDebounce(lastText: String) {
        if (searchText == lastText) {
            return
        }

        searchText = lastText
        handler.removeCallbacksAndMessages(Any())
        val searchRunnable = Runnable { searchTrackRequest(lastText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, Any(), postTime)
    }

    fun searchWithoutDebounce(changedText: String) {
        mutableIsClickAllowedLiveData.value = clickDebounce()
        searchText = changedText
        handler.removeCallbacksAndMessages(Any())
        val searchRunnable = Runnable { searchTrackRequest(changedText) }
        handler.post(searchRunnable)
    }

    private fun searchTrackRequest(lastText: String) {
        if (lastText.isNotEmpty()) {
            renderState(TrackSearchState.Loading)
            trackSearchInteractor.searchTrack(lastText, object : TracksInteractor.TrackConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        val tracks = mutableListOf<TrackUi>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks.map { TrackToTrackUi().map(it) })
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    TrackSearchState.Error(
                                        errorMessage = getApplication<Application>().getString(R.string.noInternet)
                                    )
                                )
                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    TrackSearchState.Empty(
                                        message = getApplication<Application>().getString(R.string.nothingWasFound)
                                    )
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
                }
            )
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true }, CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun trackHistory(): MutableList<TrackUi> {
        return trackHistoryInteractor.getHistory().map { TrackToTrackUi().map(it) } as MutableList<TrackUi>
    }

    private fun saveHistory() {
        trackHistoryInteractor.saveHistory()
    }

    fun onItemClick(track: TrackUi) {
        mutableIsClickAllowedLiveData.value = clickDebounce()
        trackHistoryInteractor.addTrack(TrackUiToDomain().map(track))
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

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(Any())
        saveHistory()
    }
}