package com.example.playlistmaker.main.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.activity.MediaActivity
import com.example.playlistmaker.ui.old.SearchActivity
import com.example.playlistmaker.ui.old.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnSearch: Button
    private lateinit var btnMedia: Button
    private lateinit var btnSettings: Button

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch = findViewById(R.id.btnSearch)
        btnMedia = findViewById(R.id.btnMedia)
        btnSettings = findViewById(R.id.btnSettings)

        btnSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        btnMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        btnSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}