package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnBackToMain: ImageView
    private lateinit var btnShare: ImageView
    private lateinit var btnSupport: ImageView
    private lateinit var btnUserAgreement: ImageView

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnBackToMain = findViewById(R.id.backToMain)
        btnBackToMain.setOnClickListener {
            finish()
        }

        btnShare = findViewById(R.id.share)
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Пройди курс разработчика Android приложений от Яндекс. Ссылка: https://practicum.yandex.ru/android-developer/"
            )
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        btnSupport = findViewById(R.id.support)
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

        btnUserAgreement = findViewById(R.id.userAgreement)
        btnUserAgreement.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data =
                Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(browserIntent)
        }
    }
}