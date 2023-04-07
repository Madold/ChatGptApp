package com.markusw.chatgptapp.data.network.remote.responses

data class Message(
    val role: String = "user",
    val content: String,
)