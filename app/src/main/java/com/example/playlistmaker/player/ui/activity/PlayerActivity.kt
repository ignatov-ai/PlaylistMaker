package com.example.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.model.TrackUi

@Suppress("INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
class PlayerActivity: AppCompatActivity() {

    private lateinit var track: TrackUi
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

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

        // подключаем ViewModel
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track.previewUrl)
        )[PlayerViewModel::class.java]

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

    private fun setTimer(time: String?) {
        binding.trackTimeMills.text = time
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
//        val bundle = intent.extras
//
        val cornerRadius = 8
//        var previewUrl: String? = ""
//
//        if (bundle != null) {
//            val trackImage = bundle.getString("trackImage")
//            val trackName = bundle.getString("trackName")
//            val artistName = bundle.getString("artistName")
//            val country = bundle.getString("country")
//            val trackTimeMills = bundle.getString("trackTimeMills")
//            val collectionName = bundle.getString("collectionName")
//            val releaseDate = bundle.getString("releaseDate")
//            val primaryGenreName = bundle.getString("primaryGenreName")
//            previewUrl = bundle.getString("previewUrl")

            Glide.with(this)
                .load(track.artworkUrl512)
                .placeholder(R.drawable.noalbumicon)
                .centerCrop()
                .fitCenter()
                .transform(RoundedCorners(cornerRadius))
                .into(binding.trackImage)

            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.country.text = track.country
            binding.trackTimeMills.text = track.trackTimeMillis
            binding.collectionName.text = track.collectionName
            binding.releaseDate.text = track.releaseDate
            binding.primaryGenreName.text = track.primaryGenreName
    }
}
