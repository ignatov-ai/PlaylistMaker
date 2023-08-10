package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MediaActivity : AppCompatActivity() {

    private lateinit var btnBackToMain: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        btnBackToMain = findViewById(R.id.backToMain)
        btnBackToMain.setOnClickListener{
            finish()
        }

    }
}