package com.example.playlistmaker.playlist.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.playlist.ui.model.PlaylistUi

class PlaylistsViewHolder(private val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: PlaylistUi) {
        binding.playlistName.text = playlist.playlistName
        binding.playlistTracksCount.text = playlist.getPlaylistTracksNumberText()

        Glide.with(itemView)
            .load(playlist.playlistImage)
            .placeholder(R.drawable.noalbumicon)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.margin8dp))
            )
            .into(binding.playlistIcon)
    }
}