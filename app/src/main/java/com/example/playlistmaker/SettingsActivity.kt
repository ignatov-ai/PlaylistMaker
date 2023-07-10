package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBackToMain = findViewById<ImageView>(R.id.backToMain)
        btnBackToMain.setOnClickListener {
            val displayBack = Intent(this, MainActivity::class.java)
            startActivity(displayBack)
        }

        val btnShare = findViewById<ImageView>(R.id.share)
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Пройди курс разработчика Android приложений от Яндекс. Ссылка: https://practicum.yandex.ru/android-developer/"
            )
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        val btnSupport = findViewById<ImageView>(R.id.support)
        btnSupport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:ignatov-ai@yandex.ru")
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            )
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            )
            startActivity(emailIntent)
        }

        val btnUserAgreement = findViewById<ImageView>(R.id.userAgreement)
        btnUserAgreement.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data =
                Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(browserIntent)
        }
    }
}