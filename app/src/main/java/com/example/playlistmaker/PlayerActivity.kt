package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity: AppCompatActivity() {

    val cornerRadius = 10

    private lateinit var playerBackToTracksButton: ImageView
    private lateinit var playerTrackImage: ImageView
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerAddToPlaylistButton: ImageView
    private lateinit var playerPlayPauseButton: ImageView
    private lateinit var playerLikeButton: ImageView
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

        // Получаем список из историию Выбираем первый из этого списка, т.е. последний нажатый
        var tracks: MutableList<Track> = mutableListOf()
        val sharedPrefs = getSharedPreferences(HISTORY_PREFS, MODE_PRIVATE)
        tracks = SearchTrackHistory(sharedPrefs).getHistoryList()!!.toMutableList()

        val track = tracks[0]
        val cover = track.artworkUrl512

        Glide.with(this)
            .load(cover)
            .placeholder(R.drawable.noalbumicon)
            .centerCrop()
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(playerTrackImage)

        playerTrackName.setText(track.trackName)
        playerArtistName.setText(track.artistName)
        playerCountry.setText(track.country)
        playerTrackTimeMills.setText(track.timeToMins)
        playerCollectionName.setText(track.collectionName)
        playerReleaseDate.setText(track.releaseYear)
        playerPrimaryGenreName.setText(track.primaryGenreName)
    }
}