package com.example.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.playlist.ui.recycler.PlaylistsAdapter
import com.example.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.playlist.ui.view_model.PlaylistsState
import com.example.playlistmaker.playlistShow.ui.fragment.PlaylistShowFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val DEBOUNCE_DELAY_MILLIS = 750L
    }

    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var playlistsAdapter: PlaylistsAdapter? = null

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsAdapter = PlaylistsAdapter { playlistId ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_mediaFragment_to_playlistShowFragment,
                    PlaylistShowFragment.createArgs(playlistId)
                )
            }
        }

        binding.playlistsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.playlistsRecyclerView.adapter = playlistsAdapter

        viewModel.listOfPlaylistsLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.viewCreated()

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        playlistsAdapter = null
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> {
                binding.playlistsPlaceholder.visibility = View.GONE
                binding.playlistsRecyclerView.visibility = View.VISIBLE

                playlistsAdapter?.playlists?.clear()
                playlistsAdapter?.playlists?.addAll(state.playlists)
                playlistsAdapter?.notifyDataSetChanged()
            }

            PlaylistsState.Empty -> {
                binding.playlistsPlaceholder.visibility = View.VISIBLE
                binding.playlistsRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }
}