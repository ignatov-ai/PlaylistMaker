package com.example.playlistmaker

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// https://itunes.apple.com/search?term=jack+johnson

fun main() {
    val searchQuery = "jack+johnson"

    val url = URL("https://itunes.apple.com/search?term=$searchQuery")
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val responseCode = connection.responseCode

    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        var line: String?
        val response = StringBuilder()

        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()

        println(response.toString())
    } else {
        println("Error: $responseCode")
    }

    connection.disconnect()
}