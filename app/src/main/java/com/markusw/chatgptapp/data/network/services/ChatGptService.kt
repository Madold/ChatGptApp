package com.markusw.chatgptapp.data.network.services

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.network.ChatGptApi
import com.markusw.chatgptapp.data.network.RequestBody
import com.markusw.chatgptapp.data.network.remote.responses.Message
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import javax.inject.Inject

class ChatGptService @Inject constructor(
    private val api: ChatGptApi
) {
    suspend fun getPromptResponse(prompts: List<Message>): Resource<PromptResponse> {
        return try {

            val body = RequestBody(messages = prompts)
            val call = api.getPromptResponse(body)
            val botResponse = call.execute()

            if (!botResponse.isSuccessful) {
                return Resource.Error("Connection error, check your internet connection and try again.")
            }

            Resource.Success(botResponse.body()!!)



            /*
            //For testing purposes, you can use the code below to simulate a response from the API.
            delay(2000)

            Resource.Success(
                data = PromptResponse(
                    listOf(
                        Choice(
                            finishReason = "stop",
                            index = 0,
                            message = Message(content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis molestie condimentum ipsum et molestie. Curabitur mauris libero, volutpat ut posuere in, ultricies non erat. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec viverra velit sit amet dui tristique, at euismod eros finibus. Nunc facilisis, enim eu cursus tristique, mi diam aliquet purus, ut interdum quam sapien non metus. Sed vel gravida velit, vestibulum tempor metus. Vivamus bibendum, elit ac consectetur ultrices, velit nunc pretium magna, non ornare lacus tortor ut risus. Ut auctor lectus vel diam condimentum fermentum. Maecenas ullamcorper, lectus eu elementum scelerisque, erat metus ultricies nunc, lacinia auctor neque mauris non nisi. Curabitur id vulputate augue. Interdum et malesuada fames ac ante ipsum primis in faucibus.")
                        )
                    )
                )
            )*/
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }
}