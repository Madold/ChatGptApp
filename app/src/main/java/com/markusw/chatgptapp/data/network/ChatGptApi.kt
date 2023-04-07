package com.markusw.chatgptapp.data.network

import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatGptApi {

    @POST("v1/chat/completions")
    fun getPromptResponse(@Body body: RequestBody): Call<PromptResponse>

}