package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    val cornerRadius = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Кнопка назад к трекам
        binding.backToTracks.setOnClickListener {
            finish()
        }

        // Получаем список из историию Выбираем первый из этого списка, т.е. последний нажатый
        var tracks: MutableList<Track> = mutableListOf()
        val sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
        tracks = SearchTrackHistory(sharedPrefs).getHistoryList()!!.toMutableList()

        val track = tracks.first()
        val cover = track.artworkUrl512

        Glide.with(this)
            .load(cover)
            .placeholder(R.drawable.noalbumicon)
            .centerCrop()
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.trackImage)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.country.text = track.country
        binding.trackTimeMills.text = track.timeToMins
        binding.collectionName.text = track.collectionName
        binding.releaseDate.text = track.releaseYear
        binding.primaryGenreName.text = track.primaryGenreName
    }
}