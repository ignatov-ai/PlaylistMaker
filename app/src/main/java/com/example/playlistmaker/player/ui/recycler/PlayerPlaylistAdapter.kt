package com.example.playlistmaker.player.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBottomSheetItemBinding
import com.example.playlistmaker.playlist.ui.model.PlaylistUi

class PlayerPlaylistAdapter(private val onPlaylistClickListener: OnPlaylistClickListener? = null) :
    RecyclerView.Adapter<PlaylistInPlayerViewHolder>() {

    var playlists = ArrayList<PlaylistUi>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInPlayerViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistInPlayerViewHolder(
            PlaylistBottomSheetItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistInPlayerViewHolder, position: Int) {
        holder.bind(playlists[position])

        holder.itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(playlists[position])
        }
    }

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlist: PlaylistUi)
    }
}
