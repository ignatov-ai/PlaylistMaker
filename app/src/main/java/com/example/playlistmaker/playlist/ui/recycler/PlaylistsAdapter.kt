package com.example.playlistmaker.playlist.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.playlist.ui.model.PlaylistUi

class PlaylistsAdapter(private val onPlaylistClickListener: OnPlaylistClickListener? = null) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    var playlists = ArrayList<PlaylistUi>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])

        holder.itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(playlistId = playlists[position].playlistId)
        }
    }

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlistId: Long)
    }
}