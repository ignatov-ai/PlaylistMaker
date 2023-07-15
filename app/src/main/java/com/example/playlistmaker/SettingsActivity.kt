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
                getString(R.string.shareMessage)
            )
            startActivity(Intent.createChooser(shareIntent, getString(R.string.shareLabel)))
        }

        btnSupport = findViewById(R.id.support)
        btnSupport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.mailto))
            )
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.mailTheme)
            )
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.mailMessage)
            )
            startActivity(emailIntent)
        }

        btnUserAgreement = findViewById(R.id.userAgreement)
        btnUserAgreement.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data =
                Uri.parse(getString(R.string.agreementLink))
            startActivity(browserIntent)
        }
    }
}