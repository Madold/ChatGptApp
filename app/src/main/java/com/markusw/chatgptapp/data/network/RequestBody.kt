package com.markusw.chatgptapp.data.network

import com.markusw.chatgptapp.data.network.remote.responses.Message

data class RequestBody(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val stream: Boolean = true
)

