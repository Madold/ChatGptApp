package com.markusw.chatgptapp.data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface ChatGptApi {
    @POST("v1/chat/completions")
    @Streaming
    fun getPromptResponse(@Body body: RequestBody): Call<ResponseBody>
}