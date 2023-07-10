package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btn_back_to_main = findViewById<ImageView>(R.id.backToMain)

        btn_back_to_main.setOnClickListener{
            val displayBack = Intent(this, MainActivity::class.java)
            startActivity(displayBack)
        }

    }
}