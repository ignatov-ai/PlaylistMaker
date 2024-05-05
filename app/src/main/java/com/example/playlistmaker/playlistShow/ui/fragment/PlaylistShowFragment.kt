package com.example.playlistmaker.playlistShow.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistShowBinding
import com.example.playlistmaker.player.ui.fragments.PlayerFragment
import com.example.playlistmaker.playlistEdit.ui.fragment.PlaylistEditFragment
import com.example.playlistmaker.playlistShow.ui.model.PlaylistShowElement
import com.example.playlistmaker.playlistShow.ui.recycler.TrackAdapterLongClick
import com.example.playlistmaker.playlistShow.ui.view_model.PlaylistShowViewModel
import com.example.playlistmaker.search.ui.model.TrackUi
import com.example.playlistmaker.search.ui.view_model.SearchViewModel.Companion.CLICK_DEBOUNCE_DELAY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistShowFragment : Fragment() {

    private var _binding: FragmentPlaylistShowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistShowViewModel by viewModel {
        parametersOf(arguments?.getLong(PLAYLISTID))
    }
    private var trackAdapter: TrackAdapterLongClick? = null
    private var isClickAllowed = true
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>
    private lateinit var tracksBottomSheet: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tracksList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        trackAdapter = TrackAdapterLongClick(
            OnItemClickListener = {
                if (clickDebounce()) {
                    findNavController().navigate(
                        R.id.action_playlistShowFragment_to_playerFragment,
                        PlayerFragment.createArgs(it)
                    )
                }
            },
            onTrackLongClickListener = {
                showDialog(it)
            }
        )
        binding.tracksList.adapter = trackAdapter

        viewModel.playlistShowLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.buttonBackPlaylist.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.shareIcon.setOnClickListener {
            share()
        }

        binding.sharePlaylistText.setOnClickListener {
            share()
        }

        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.bottomSheetMenu)
        tracksBottomSheet = BottomSheetBehavior.from(binding.tracksBottomSheet)

        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        binding.settingsIcon.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        binding.overlay.isClickable = false
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        binding.overlay.isClickable = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
        binding.shareIcon.setOnClickListener {
            share()
        }
        binding.editPlaylistText.setOnClickListener {
            val playlistMainItem = viewModel.playlistShowLiveData.value?.copy(tracks = emptyList())
            findNavController().navigate(
                R.id.action_playlistShowFragment_to_playlistEditFragment,
                PlaylistEditFragment.createArgs(
                    playlistMainItem!!
                )
            )
        }
        binding.deletePlaylistText.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            showDeleteDialog()
        }

        binding.shareLine.doOnNextLayout {
            val screenHeight = binding.root.measuredHeight
            val tracksBottomSheetHeight = screenHeight - binding.shareLine.bottom
            val bottomSheetMenuHeight = screenHeight - binding.playlistNameLine.bottom

            tracksBottomSheet.setPeekHeight(tracksBottomSheetHeight, true)
            bottomSheetBehaviorMenu.setPeekHeight(bottomSheetMenuHeight, true)
        }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(resources.getString(R.string.deletePlaylist))
            .setMessage(getString(R.string.deletePlaylistQuestion))
            .setNegativeButton(getString(R.string.no)) { dialog, which ->

            }.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.playlistShowLiveData.removeObservers(viewLifecycleOwner)
                viewModel.deletePlaylistClicked()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun share() {
        if (trackAdapter?.tracks.isNullOrEmpty()) {
            Snackbar.make(binding.root, getString(R.string.emptyPlaylist), Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(Color.GRAY)
                .show()
        } else {
            viewModel.shareClicked(binding.playlistTracksCount.text.toString())
        }
    }

    private fun showDialog(track: TrackUi) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setMessage(getString(R.string.trackDeleteQuestion))
            .setNegativeButton(getString(R.string.no)) { dialog, which ->

            }.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                val position = trackAdapter?.tracks?.indexOf(track)
                trackAdapter?.tracks?.remove(track)
                trackAdapter?.notifyItemRemoved(position!!)
                trackAdapter?.notifyItemRangeChanged(position!!, trackAdapter?.tracks?.size!!)
                viewModel.trackRemoved(track)
            }
            .show()
    }

    private fun render(playlistShowElement: PlaylistShowElement) {
        if (binding.playlistName.text != playlistShowElement.playlistName)
            binding.playlistName.text = playlistShowElement.playlistName
        if (binding.playlistDescription.text != playlistShowElement.playlistDescription)
            binding.playlistDescription.text = playlistShowElement.playlistDescription
        if (binding.playlistDescription.text == "") {
            binding.playlistDescription.visibility = View.GONE
        } else {
            binding.playlistDescription.visibility = View.VISIBLE
        }
        val duration = "${playlistShowElement.playlistDuration} ${
            resources.getQuantityString(
                R.plurals.minutesText,
                playlistShowElement.playlistDuration.toInt()
            )
        }"
        if (binding.playlistDuration.text != duration)
            binding.playlistDuration.text = duration
        val countTracks = "${playlistShowElement.countTracks} ${
            resources.getQuantityString(
                R.plurals.tracksCount,
                playlistShowElement.countTracks
            )
        }"
        if (binding.playlistTracksCount.text != countTracks)
            binding.playlistTracksCount.text = countTracks
        Glide.with(this)
            .load(playlistShowElement.playlistCoverPath)
            .placeholder(R.drawable.noalbumicon)
            .transform(CenterCrop())
            .into(binding.playlistImage)
        if (trackAdapter?.tracks.isNullOrEmpty()) {
            trackAdapter?.tracks?.addAll(playlistShowElement.tracks)
            trackAdapter?.notifyDataSetChanged()
        }
        if (binding.menuTitle.playlistNameSmall.text != playlistShowElement.playlistName)
            binding.menuTitle.playlistNameSmall.text = playlistShowElement.playlistName
        if (binding.menuTitle.playlistTracksCountSmall.text != countTracks)
            binding.menuTitle.playlistTracksCountSmall.text = countTracks
        Glide.with(this)
            .load(playlistShowElement.playlistCoverPath)
            .placeholder(R.drawable.noalbumicon)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.size4dp))
            )
            .into(binding.menuTitle.playlistIconSmall)
        if (playlistShowElement.tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.emptyPlaylistToast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackAdapter = null
        binding.tracksList.adapter = null
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {

        private const val PLAYLISTID = "playlistId"
        fun createArgs(playlistId: Long): Bundle = bundleOf(PLAYLISTID to playlistId)
    }
}