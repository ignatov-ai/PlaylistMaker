package com.example.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity: AppCompatActivity() {

    private lateinit var playerBackToTracksButton: Button
    private lateinit var playerTrackImage: ImageView
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerAddToPlaylistButton: ImageButton
    private lateinit var playerPlayPauseButton: ImageButton
    private lateinit var playerLikeButton: ImageButton
    private lateinit var playerPlayedTime: TextView
    private lateinit var playerTrackTimeMills: TextView
    private lateinit var playerCollectionName: TextView
    private lateinit var playerReleaseDate: TextView
    private lateinit var playerPrimaryGenreName: TextView
    private lateinit var playerCountry: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerBackToTracksButton = findViewById(R.id.backToTracks)
        playerTrackImage = findViewById(R.id.trackImage)
        playerTrackName = findViewById(R.id.trackName)
        playerArtistName = findViewById(R.id.artistName)
        playerAddToPlaylistButton = findViewById(R.id.addToPlaylistButton)
        playerLikeButton = findViewById(R.id.likeButton)
        playerPlayPauseButton = findViewById(R.id.playPauseButton)
        playerPlayedTime = findViewById(R.id.playedTime)
        playerTrackTimeMills = findViewById(R.id.trackTimeMills)
        playerCollectionName = findViewById(R.id.collectionName)
        playerReleaseDate = findViewById(R.id.releaseDate)
        playerPrimaryGenreName = findViewById(R.id.primaryGenreName)
        playerCountry = findViewById(R.id.country)

        // Кнопка назад к трекам
        playerBackToTracksButton.setOnClickListener {
            finish()
        }



    }
}