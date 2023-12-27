package com.example.playlistmaker.player.ui.fragments

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackToTrackUi
import com.example.playlistmaker.search.ui.model.TrackUi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment: Fragment() {

    companion object {
        private const val TRACK = "TRACK"

        fun createArgs(track: TrackUi): Bundle =
            bundleOf(TRACK to track)
    }

    private lateinit var track: TrackUi
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track.previewUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToTracks.setOnClickListener {
            findNavController().navigateUp()
        }

        track = getTrack()
        viewModel.playerStateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.playerPositionLiveData.observe(viewLifecycleOwner) {
            setTimer(it)
        }

        // Кнопка плей/пауза к трекам
        setTrackInfo()

        binding.playPauseButton.setOnClickListener {
            viewModel.onPlayerButtonClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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
            arguments?.getParcelable(TRACK, TrackUi::class.java)
                ?: TrackToTrackUi().map(
                    Track()
                )
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(TRACK) ?: TrackToTrackUi().map(
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
