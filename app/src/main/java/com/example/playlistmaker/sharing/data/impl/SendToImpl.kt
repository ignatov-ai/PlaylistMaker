package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.data.SendTo

class SendToImpl (private val context: Context) : SendTo {
    override fun share(email: Array<String>, subject: String, text: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(emailIntent)
    }
}