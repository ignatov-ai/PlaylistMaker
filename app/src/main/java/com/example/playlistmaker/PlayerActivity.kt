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

        // получаем данные для отображения с предыдущего экрана
        val bundle = intent.extras
        if (bundle != null) {
            val trackImage = bundle.getString("trackImage")
            val trackName = bundle.getString("trackName")
            val artistName = bundle.getString("artistName")
            val country = bundle.getString("country")
            val trackTimeMills = bundle.getString("trackTimeMills")
            val collectionName = bundle.getString("collectionName")
            val releaseDate = bundle.getString("releaseDate")
            val primaryGenreName = bundle.getString("primaryGenreName")

            Glide.with(this)
                .load(trackImage)
                .placeholder(R.drawable.noalbumicon)
                .centerCrop()
                .fitCenter()
                .transform(RoundedCorners(cornerRadius))
                .into(binding.trackImage)

            binding.trackName.text = trackName
            binding.artistName.text = artistName
            binding.country.text = country
            binding.trackTimeMills.text = trackTimeMills
            binding.collectionName.text = collectionName
            binding.releaseDate.text = releaseDate
            binding.primaryGenreName.text = primaryGenreName
        }
    }
}