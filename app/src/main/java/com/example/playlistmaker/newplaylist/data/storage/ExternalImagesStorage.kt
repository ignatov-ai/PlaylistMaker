package com.example.playlistmaker.newplaylist.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.newplaylist.data.ImagesStorage
import java.io.File
import java.io.FileOutputStream

class ExternalImagesStorage(private val context: Context) : ImagesStorage {
    override fun saveImage(path: String, imageId: String) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILE_DIRECTORY)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "playlist-$imageId.jpg")
        val inputStream = context.contentResolver?.openInputStream(Uri.parse(path))
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
    }

    companion object {
        const val FILE_DIRECTORY = "playlistIcons"
    }
}