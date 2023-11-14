package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(trackUrl: String, private val mediaPlayerInteractor: PlayerInteractor) : ViewModel() {
    companion object {
        private const val DELAY = 300L
    }

    private var mutablePlayerStateLiveData = MutableLiveData<PlayerState>()
    val playerStateLiveData: LiveData<PlayerState> = mutablePlayerStateLiveData

    private var mutablePlayerPositionLiveData = MutableLiveData<String>()
    val playerPositionLiveData: LiveData<String> = mutablePlayerPositionLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable{
        timerUpdate()
    }

    private fun timerUpdate() {
        mutablePlayerPositionLiveData.value = getCurrentPosition()
        handler.postDelayed(timerRunnable, DELAY)
    }

    private fun changePlayerState(playerState: PlayerState) {
        mutablePlayerStateLiveData.value = playerState
    }

    init{
        changePlayerState(PlayerState.STATE_DEFAULT)

        mediaPlayerInteractor.preparePlayer(trackUrl)

        val onPreparedListener = object : PlayerInteractor.PreparedListener{
            override fun setOnPreparedListener() {
                changePlayerState(PlayerState.STATE_PREPARED)
            }
        }

        mediaPlayerInteractor.setOnPreparedListener(onPreparedListener)

        val onCompletedListener = object : PlayerInteractor.CompletionListener{
            override fun setOnCompletionListener() {
                handler.removeCallbacks(timerRunnable)
                changePlayerState(PlayerState.STATE_PREPARED)
                mutablePlayerPositionLiveData.value = "00:00"
            }
        }

        mediaPlayerInteractor.setOnCompletionListener(onCompletedListener)
    }

    fun onPlayerButtonClick(){
        when(mutablePlayerStateLiveData.value){
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

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        changePlayerState(PlayerState.STATE_PAUSED)
        handler.removeCallbacks(timerRunnable)
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayerInteractor.currentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(timerRunnable)
        mediaPlayerInteractor.stopPlayer()
    }

    fun stopPlayer() {
        mediaPlayerInteractor.stopPlayer()
    }
}