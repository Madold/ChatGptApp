package com.markusw.chatgptapp.data.network.remote.responses

data class Choice(
    val delta: Delta
)

data class Delta(
    val role: String?,
    val content: String?
)