package com.example.playlistmaker.search.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackListElementBinding
import com.example.playlistmaker.search.ui.model.TrackUi

class TrackAdapter(private val recycleViewListener: OnItemClickListener? = null) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<TrackUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackListElementBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            recycleViewListener?.onItemClick(tracks[position])
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(track: TrackUi)
    }





}