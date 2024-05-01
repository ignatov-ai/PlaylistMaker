package com.example.playlistmaker.playlistShow.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlistShow.domain.api.PlaylistShowInteractor
import com.example.playlistmaker.playlistShow.ui.model.PlaylistShowElement
import com.example.playlistmaker.search.ui.mapper.TrackUiMapper
import com.example.playlistmaker.search.ui.model.TrackUi
import com.example.playlistmaker.sharing.domain.impl.SendUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PlaylistShowViewModel(
    private val playlistId: Long,
    private val playlistShowInteractor: PlaylistShowInteractor,
    private val sendUseCase: SendUseCase,
) : ViewModel() {

    private var mutablePlaylistShowLiveData = MutableLiveData<PlaylistShowElement>()
    val playlistShowLiveData: LiveData<PlaylistShowElement> = mutablePlaylistShowLiveData
    private lateinit var jobGetPlaylistInfo: Job

    init {
        jobGetPlaylistInfo = viewModelScope.launch(Dispatchers.IO) {
            playlistShowInteractor.getPlaylistShow(playlistId)
                .distinctUntilChanged()
                .collect { playlistShow ->
                    mutablePlaylistShowLiveData.postValue(
                        PlaylistShowElement(
                            playlistId = playlistShow.playlist.playlistId!!,
                            playlistName = playlistShow.playlist.playlistName,
                            playlistDescription = playlistShow.playlist.playlistDescription,
                            playlistCoverPath = playlistShow.playlist.playlistCoverUri,
                            countTracks = playlistShow.playlist.countTracks,
                            playlistDuration = playlistShow.tracks.sumOf { track ->
                                track.trackTimeMillis
                            } / 1000 / 60,
                            tracks = playlistShow.tracks.map { track ->
                                TrackUiMapper.map(track)
                            }
                        )
                    )
                }
        }
    }

    fun trackRemoved(track: TrackUi) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistShowInteractor.removeTrackFromPlaylist(playlistId, track.trackId)
        }
    }

    fun shareClicked(tracksInPlaylist: String) {
        val message = """
            |${playlistShowLiveData.value?.playlistName}
            |${playlistShowLiveData.value?.playlistDescription}
            |$tracksInPlaylist
            |${trackListStringHandle()}
        """.trimMargin()
        sendUseCase.execute(message)
    }

    private fun trackListStringHandle(): String {
        val stringBuilder = StringBuilder()
        val list = playlistShowLiveData.value?.tracks!!
        for ((index, value) in list.withIndex()) {
            stringBuilder.append("${index + 1}. ${value.artistName} - ${value.trackName} (${value.timeToMins})\n")
        }
        return stringBuilder.toString()
    }

    fun deletePlaylistClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            jobGetPlaylistInfo?.cancelAndJoin()
            playlistShowInteractor.deletePlaylist(playlistId)
        }
    }
}