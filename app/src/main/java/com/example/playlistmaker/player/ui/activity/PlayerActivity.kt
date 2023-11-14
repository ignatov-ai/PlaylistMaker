package com.example.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.model.TrackUi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity: AppCompatActivity() {

    private lateinit var track: TrackUi
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track.previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Кнопка назад к трекам
        binding.backToTracks.setOnClickListener {
            finish()
        }

        track = getTrack()
        println(track)


        viewModel.playerStateLiveData.observe(this) {
            render(it)
        }

        viewModel.playerPositionLiveData.observe(this) {
            setTimer(it)
        }

        // Кнопка плей/пауза к трекам
        binding.playPauseButton.setOnClickListener {
            viewModel.onPlayerButtonClick()
        }

        setTrackInfo()

        binding.playPauseButton.setOnClickListener {
            viewModel.onPlayerButtonClick()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPlayer()
    }

    private fun setTimer(time: String?) {
        binding.playedTime.text = time
    }

    private fun render(state: PlayerState) {
        when(state){
            PlayerState.STATE_DEFAULT -> renderStateDefault()
            PlayerState.STATE_PREPARED -> renderStatePrepared()
            PlayerState.STATE_PLAYING -> renderStatePlaying()
            PlayerState.STATE_PAUSED -> renderStatePaused()
        }
    }

    private fun renderStatePaused() {
        binding.playPauseButton.setImageResource(R.drawable.playbutton)
    }

    private fun renderStatePlaying() {
        binding.playPauseButton.setImageResource(R.drawable.pausebutton)
    }

    private fun renderStatePrepared() {
        binding.playPauseButton.isEnabled = true
        binding.playPauseButton.setImageResource(R.drawable.playbutton)
    }

    private fun renderStateDefault() {
        binding.playPauseButton.isEnabled = false
    }

    private fun getTrack(): TrackUi =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, TrackUi::class.java)
                ?: TrackToTrackUi().map(
                    Track()
                )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(TRACK) ?: TrackToTrackUi().map(
                Track()
            )
        }

    private fun setTrackInfo() {
        val cornerRadius = 8

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.noalbumicon)
            .centerCrop()
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.trackImage)

        val timeToMins = track.timeToMins
        val releaseYear = track.releaseYear

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.country.text = track.country
        binding.trackTimeMills.text = timeToMins
        binding.collectionName.text = track.collectionName
        binding.releaseDate.text = releaseYear
        binding.primaryGenreName.text = track.primaryGenreName
    }
}
