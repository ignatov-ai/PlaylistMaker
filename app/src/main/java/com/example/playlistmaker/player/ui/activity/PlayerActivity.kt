package com.example.playlistmaker.player.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity: AppCompatActivity() {

    companion object {
        private const val DELAY = 500L
    }

    private lateinit var playButton : ImageView
    private lateinit var playedTime : TextView

    private var playerState = PlayerState.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable{
        override fun run() {
            if (mediaPlayer.isPlaying){
                playedTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, DELAY)
            }
        }
    }

    private lateinit var binding: ActivityPlayerBinding
    private val cornerRadius = 8
    private var previewUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

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
            previewUrl = bundle.getString("previewUrl")

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

        playButton = findViewById(R.id.playPauseButton)
        playedTime = findViewById(R.id.playedTime)

        preparePlayer()
        playButton.setOnClickListener{
            Log.d("playButton_","play")
            playbackControl()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            playButton.setImageResource(R.drawable.playbutton)
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.playbutton)
            playerState = PlayerState.STATE_PREPARED
            playedTime.text = "00:00"
            handler.removeCallbacks(runnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pausebutton)
        playerState = PlayerState.STATE_PLAYING
        handler.post(runnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.playbutton)
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    private fun playbackControl(playerState: PlayerState) {
        when(playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}