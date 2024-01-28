package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favourites.domain.FavouritesInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.mapper.TrackUiToDomain
import com.example.playlistmaker.search.ui.model.TrackUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayerViewModel(
    private val track: TrackUi,
    private val mediaPlayerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor,
    private val trackUiToDomain: TrackUiToDomain
    ): ViewModel() {
    companion object {
        private const val DELAY = 300L
    }

    private var mutablePlayerStateLiveData = MutableLiveData<PlayerState>()
    val playerStateLiveData: LiveData<PlayerState> = mutablePlayerStateLiveData

    private var mutablePlayerPositionLiveData = MutableLiveData<String>()
    val playerPositionLiveData: LiveData<String> = mutablePlayerPositionLiveData

    private var timerJob: Job? = null

    private var mutableFavouriteLiveData = MutableLiveData<Boolean>()
    val favouriteListLiveData: LiveData<Boolean> = mutableFavouriteLiveData

    private fun timerUpdate() {
        timerJob = viewModelScope.launch {
            while (mutablePlayerStateLiveData.value == PlayerState.STATE_PLAYING) {
                mutablePlayerPositionLiveData.value = getCurrentPosition()
                delay(DELAY)
            }
        }
    }

    private fun changePlayerState(playerState: PlayerState) {
        mutablePlayerStateLiveData.value = playerState
    }

    init {
        changePlayerState(PlayerState.STATE_DEFAULT)

        mediaPlayerInteractor.preparePlayer(track.previewUrl)

        val onPreparedListener = object : PlayerInteractor.PreparedListener {
            override fun setOnPreparedListener() {
                changePlayerState(PlayerState.STATE_PREPARED)
            }
        }

        mediaPlayerInteractor.setOnPreparedListener(onPreparedListener)

        val onCompletedListener = object : PlayerInteractor.CompletionListener {
            override fun setOnCompletionListener() {
                changePlayerState(PlayerState.STATE_PREPARED)
                val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
                mutablePlayerPositionLiveData.value = formatter.format(Date(0))
            }
        }

        mediaPlayerInteractor.setOnCompletionListener(onCompletedListener)
    }

    fun onPlayerButtonClick() {
        when (mutablePlayerStateLiveData.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            else -> Unit
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        changePlayerState(PlayerState.STATE_PLAYING)
        timerUpdate()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        changePlayerState(PlayerState.STATE_PAUSED)
        timerJob?.cancel()
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayerInteractor.currentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.stopPlayer()
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track.isFavourite) {
                track.isFavourite = false
                favouritesInteractor.deleteTrackFromFavourites(trackUiToDomain.map(track))
                mutableFavouriteLiveData.postValue(false)
            } else {
                track.isFavourite = true
                favouritesInteractor.addTrackToFavourites(trackUiToDomain.map(track))
                mutableFavouriteLiveData.postValue(true)
            }
        }
    }
}