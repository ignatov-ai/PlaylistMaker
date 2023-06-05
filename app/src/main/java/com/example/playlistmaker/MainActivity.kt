package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_search = findViewById<Button>(R.id.btn_search)
        val btn_media = findViewById<Button>(R.id.btn_media)
        val btn_settings = findViewById<Button>(R.id.btn_settings)

        val btnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку ПОИСК!", Toast.LENGTH_SHORT).show()
            }
        }
        btn_search.setOnClickListener(btnClickListener)

        btn_media.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку МЕДИАТЕКА!", Toast.LENGTH_SHORT).show()
        }

        btn_settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку НАСТРОЙКИ!", Toast.LENGTH_SHORT).show()
        }

    }
}