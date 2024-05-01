package com.example.playlistmaker.playlistEdit.ui.fragment

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.newplaylist.data.storage.ExternalImagesStorage.Companion.FILE_DIRECTORY
import com.example.playlistmaker.newplaylist.ui.fragment.NewPlaylistFragment
import com.example.playlistmaker.playlistEdit.ui.view_model.PlaylistEditViewModel
import com.example.playlistmaker.playlistShow.ui.model.PlaylistShowElement
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : NewPlaylistFragment() {

    override val viewModel: PlaylistEditViewModel by viewModel()
    private var playlist: PlaylistShowElement? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = getPlaylist()
        setPlaylistInfo()
        binding.newPlaylistAddButton.setOnClickListener {
            viewModel.onButtonSaveClick(
                playlistId = playlist?.playlistId,
                playlistName = binding.newPlaylistNameField.text.toString(),
                playlistDescription = binding.newPlaylistDescriptionField.text.toString(),
                playlistCoverUri = if (imageId != "") {
                    activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path + "/$FILE_DIRECTORY/cover-$imageId.jpg"
                } else {
                    playlist?.playlistCoverPath ?: ""
                }
            )
            findNavController().navigateUp()
        }
    }

    private fun getPlaylist(): PlaylistShowElement? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PLAYLIST, PlaylistShowElement::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(PLAYLIST)
        }

    private fun setPlaylistInfo() {
        binding.newPlaylistNameField.setText(playlist?.playlistName)
        binding.newPlaylistDescriptionField.setText(playlist?.playlistDescription)
        Glide.with(this)
            .load(playlist?.playlistCoverPath)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin8dp))
            )
            .into(binding.newPlaylistIcon)
        binding.newPlaylistAddButton.setText(getString(R.string.save))
        binding.newPlayListHeader.setText(getString(R.string.edit))
    }

    override fun backPressedHandle() {
        findNavController().navigateUp()
    }

    companion object {
        private const val PLAYLIST = "PLAYLIST"
        fun createArgs(playlist: PlaylistShowElement): Bundle =
            bundleOf(PLAYLIST to playlist)
    }
}