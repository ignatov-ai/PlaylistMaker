package com.example.playlistmaker.player.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistBottomSheetItemBinding
import com.example.playlistmaker.playlist.ui.model.PlaylistUi

class PlaylistInPlayerViewHolder(private val binding: PlaylistBottomSheetItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: PlaylistUi) {
        binding.playlistNameSmall.text = playlist.playlistName
        binding.playlistTracksCountSmall.text = playlist.getPlaylistTracksNumberText()
        Glide.with(itemView)
            .load(playlist.playlistImage)
            .placeholder(R.drawable.noalbumicon)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.size4dp))
            )
            .into(binding.playlistIconSmall)
    }
}