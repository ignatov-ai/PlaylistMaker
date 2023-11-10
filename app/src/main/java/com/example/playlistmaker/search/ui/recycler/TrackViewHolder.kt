package com.example.playlistmaker.search.ui.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackListElementBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.TrackUi
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackListElementBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(track: TrackUi) {
        binding.trackListElementName.text = track.trackName
        binding.trackListElementPropertiesArtist.text = track.artistName
        binding.trackListElementPropertiesTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())


        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.noalbumicon)
            .centerCrop()
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(binding.trackListElementIcon)
    }
}