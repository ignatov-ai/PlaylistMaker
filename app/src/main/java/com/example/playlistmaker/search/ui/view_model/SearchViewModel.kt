package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide.init
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.mapper.TrackUiToDomain
import com.example.playlistmaker.search.ui.model.TrackUi

class SearchViewModel(
    private val trackInteractor: TracksInteractor,
    private val trackHistoryInteractor: TracksHistoryInteractor) : ViewModel() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L

        const val ERROR_MESSAGE = "Проблемы со связью\\n\\nЗагрузка не удалась. Проверьте подключение к интернету"
        const val MESSAGE = "Ничего не нашлось"

        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    trackInteractor = Creator.provideTrackInteractor(context),
                    trackHistoryInteractor = Creator.provideTracksHistoryInteractor(context)
                )
            }
        }

    }

    private var searchText: String? = null

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
            trackInteractor.searchTrack(lastText, object : TracksInteractor.TrackConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        val tracks = mutableListOf<TrackUi>()
                        if (foundTracks != null) {
                            tracks.addAll(foundTracks.map { TrackToTrackUi().map(it) })
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    TrackSearchState.Error(
                                        errorMessage =  ERROR_MESSAGE)
                                    )

                            }

                            tracks.isEmpty() -> {
                                renderState(
                                    TrackSearchState.Empty(
                                        message = MESSAGE)
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