package com.example.playlistmaker.favourites.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.favourites.ui.view_model.FavouriteState
import com.example.playlistmaker.favourites.ui.view_model.FavouritesViewModel
import com.example.playlistmaker.player.ui.fragments.PlayerFragment
import com.example.playlistmaker.search.ui.model.TrackUi
import com.example.playlistmaker.search.ui.recycler.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteFragment()
        const val CLICK_DEBOUNCE_DELAY = 750L
    }

    private val viewModel: FavouritesViewModel by viewModel()

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private var tracksAdapter: TrackAdapter? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksAdapter = TrackAdapter {
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_mediaFragment3_to_playerFragment,
                    PlayerFragment.createArgs(it)
                ).apply { it.isFavourite = true }
            }
        }



        //адаптеры списков
        binding.favouritesTracksListView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.favouritesTracksListView.adapter = tracksAdapter

        viewModel.favouritesListOfLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.onViewCreatedOnScreen()
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

    override fun onDestroyView() {
        super.onDestroyView()
        tracksAdapter = null
        _binding = null
    }

    private fun render(state: FavouriteState) {
        when (state) {
            is FavouriteState.Content -> showFavouriteTrackList(state.tracks)
            FavouriteState.Empty -> emptyFavouritePlaceholder()
        }
    }

    private fun emptyFavouritePlaceholder() {
        binding.favouritesPlaceholder.visibility = View.VISIBLE
        binding.favouritesTracksListView.visibility = View.GONE
    }

    private fun showFavouriteTrackList(tracks: List<TrackUi>) {
        binding.favouritesPlaceholder.visibility = View.GONE
        binding.favouritesTracksListView.visibility = View.VISIBLE

        tracksAdapter?.tracks?.clear()
        tracksAdapter?.tracks?.addAll(tracks)
        tracksAdapter?.notifyDataSetChanged()
    }
}