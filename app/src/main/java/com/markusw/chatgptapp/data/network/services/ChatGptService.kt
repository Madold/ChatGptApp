package com.markusw.chatgptapp.data.network.services

import com.google.gson.Gson
import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.network.ChatGptApi
import com.markusw.chatgptapp.data.network.RequestBody
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import com.markusw.chatgptapp.domain.ServerEvent
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChatGptService @Inject constructor(
    private val api: ChatGptApi
) {
    suspend fun getPromptResponse(prompts: List<Message>): Resource<Flow<ServerEvent>> {
        return try {
            val body = RequestBody(messages = prompts)
            val call = api.getPromptResponse(body)
            val botResponse = call.execute()
            val gson = Gson()

            if (!botResponse.isSuccessful) {

                Logger.d("Bot response not successful, error code: ${botResponse.code()}")

                if (botResponse.code() == 429) {
                    return Resource.Error("Too many requests, take a moment.")
                }
                return Resource.Error("Connection error, check your internet connection and try again.")
            }

            return Resource.Success(
                flow {
                    val input = botResponse.body()?.byteStream()?.bufferedReader()
                        ?: throw Exception()
                    var line = input.readLine() ?: throw Exception()
                    while (line != "data: [DONE]") {
                        line = input.readLine() ?: continue
                        if (line.startsWith("data: ")) {
                            line.removePrefix("data: ").also {
                                if (it != "[DONE]" && it.isNotBlank()) {
                                    gson.fromJson(it, PromptResponse::class.java)
                                        .also { promptResponse ->
                                            promptResponse.choices[0].delta.content?.let {
                                                emit(
                                                    ServerEvent.BotResponse(
                                                        promptResponse
                                                    )
                                                )
                                            }
                                        }
                                }
                            }
                        }
                    }
                    emit(ServerEvent.BotFinishedTyping)
                    input.close()
                }.flowOn(Dispatchers.IO)
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }
}