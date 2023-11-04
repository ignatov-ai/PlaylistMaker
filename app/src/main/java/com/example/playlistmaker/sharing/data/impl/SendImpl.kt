package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.playlistmaker.sharing.data.Send

class SendImpl(private val context: Context) : Send {
    override fun share(text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        val shareIntent = Intent.createChooser(intent, "Share via")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }
}