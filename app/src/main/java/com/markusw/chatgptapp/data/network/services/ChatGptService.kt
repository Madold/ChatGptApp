package com.markusw.chatgptapp.data.network.services

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.model.MessageRole
import com.markusw.chatgptapp.data.network.ChatGptApi
import com.markusw.chatgptapp.data.network.RequestBody
import com.markusw.chatgptapp.data.network.remote.responses.Choice
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import kotlinx.coroutines.delay
import javax.inject.Inject

class ChatGptService @Inject constructor(
    private val api: ChatGptApi
) {
    suspend fun getPromptResponse(prompt: String): Resource<PromptResponse> {
        return try {

            val body = RequestBody(
                messages = listOf(
                    Message(content = prompt)
                )
            )

            val call = api.getPromptResponse(body)
            val botResponse = call.execute()

            if (!botResponse.isSuccessful) {
                return Resource.Error(botResponse.errorBody().toString())
            }

            Resource.Success(botResponse.body()!!)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error("Connection error")
        }
    }
}