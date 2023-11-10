package com.example.playlistmaker.media.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.view_model.MediaViewModel

class MediaActivity : AppCompatActivity() {

    private lateinit var btnBackToMain: ImageView
    private lateinit var viewModel: MediaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        btnBackToMain = findViewById(R.id.backToMain)
        btnBackToMain.setOnClickListener{
            finish()
        }

        viewModel = ViewModelProvider(
            this,
            MediaViewModel.getViewModelFactory()
        )[MediaViewModel::class.java]

    }
}