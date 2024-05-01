package com.example.playlistmaker.playlistShow.ui.recycler

import com.example.playlistmaker.search.ui.model.TrackUi
import com.example.playlistmaker.search.ui.recycler.TrackAdapter
import com.example.playlistmaker.search.ui.recycler.TrackViewHolder

class TrackAdapterLongClick(
    OnItemClickListener: OnItemClickListener? = null,
    private val onTrackLongClickListener: OnTrackLongClickListener? = null
) : TrackAdapter(OnItemClickListener) {

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnLongClickListener {
            onTrackLongClickListener?.onTrackLongClick(tracks[position])
            true
        }
    }

    fun interface OnTrackLongClickListener {
        fun onTrackLongClick(track: TrackUi)
    }
}