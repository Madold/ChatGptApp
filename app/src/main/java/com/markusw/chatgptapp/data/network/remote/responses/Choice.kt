package com.markusw.chatgptapp.data.network.remote.responses

import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("finish_reason")
    val finishReason: String,
    val index: Int,
    val message: Message
)