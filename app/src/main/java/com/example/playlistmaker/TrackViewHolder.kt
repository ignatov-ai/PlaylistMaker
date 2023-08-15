package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val trackName: TextView = itemView.findViewById(R.id.trackListElementName)
    private val artistName: TextView = itemView.findViewById(R.id.trackListElementPropertiesArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackListElementPropertiesTime)
    private val trackUrl: ImageView = itemView.findViewById(R.id.trackListElementIcon)

    fun bind(track: Track, listener: TrackAdapter.RecycleViewListener) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.noalbumicon)
            .centerCrop()
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(trackUrl)

        itemView.setOnClickListener{
            listener.onItemClick(track)
        }
    }
}