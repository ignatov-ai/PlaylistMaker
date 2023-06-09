package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackListElementName)
    private val artistName: TextView = itemView.findViewById(R.id.trackListElementPropertiesArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackListElementPropertiesTime)
    private val trackUrl: ImageView = itemView.findViewById(R.id.trackListElementIcon)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.noalbumicon)
            .centerCrop()
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(trackUrl)
    }
}