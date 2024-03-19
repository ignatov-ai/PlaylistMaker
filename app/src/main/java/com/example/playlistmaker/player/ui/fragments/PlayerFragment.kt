package com.example.playlistmaker.player.ui.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.recycler.PlayerPlaylistAdapter
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.StateOfTrackInPlaylist
import com.example.playlistmaker.playlist.ui.model.PlaylistUi
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.mapper.TrackUiMapper
import com.example.playlistmaker.search.ui.model.TrackUi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
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

    private var playlistAdapter: PlayerPlaylistAdapter? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
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

        viewModel.favouriteListLiveData.observe(viewLifecycleOwner) { setLikeButtonState(it) }

        // Кнопка плей/пауза к трекам
        setTrackInfo()

        binding.playPauseButton.setOnClickListener {
            viewModel.onPlayerButtonClick()
        }
        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.listPlaylistsLiveData.observe(viewLifecycleOwner) { setListInAdapter(it) }

        viewModel.observeMessageLiveData().observe(viewLifecycleOwner) { showMessage(it) }


        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetPlaylists)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.onAddButtonClicked()
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 0.6f
            }
        })
        playlistAdapter = PlayerPlaylistAdapter { playlist ->
            viewModel.onPlaylistClicked(playlist)
        }
        binding.playerRecycleViewPlaylists.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.playerRecycleViewPlaylists.adapter = playlistAdapter
        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
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
                ?: TrackUiMapper().map(
                    Track()
                )
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(TRACK) ?: TrackUiMapper().map(
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

    private fun setLikeButtonState(isFavourite: Boolean) {
        if (isFavourite) {
            binding.likeButton.setImageResource(R.drawable.liketrackbuttontrue)
        } else {
            binding.likeButton.setImageResource(R.drawable.liketrackbuttonfalse)
        }
    }

    private fun showMessage(stateOfTrackInPlaylist: StateOfTrackInPlaylist) {
        val message: String
        when (stateOfTrackInPlaylist) {
            is StateOfTrackInPlaylist.TrackInPlaylistAdded -> {
                message = "${getString(R.string.addedToPlaylist)} ${stateOfTrackInPlaylist.playlistName}"
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            is StateOfTrackInPlaylist.TrackInPlaylistNotAdded -> {
                message = "${getString(R.string.existInPlaylist)} ${stateOfTrackInPlaylist.playlistName}"
            }

        }

        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)

        snackbar.show()
    }

    private fun setListInAdapter(playlists: List<PlaylistUi>?) {
        if (playlists != null) {
            playlistAdapter?.playlists?.clear()
            playlistAdapter?.playlists?.addAll(playlists)
            playlistAdapter?.notifyDataSetChanged()
        }
    }
}
