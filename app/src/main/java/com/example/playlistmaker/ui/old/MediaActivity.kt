package com.example.playlistmaker.ui.old

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.playlistmaker.R

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