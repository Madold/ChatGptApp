package com.markusw.chatgptapp.data.network.services

import com.markusw.chatgptapp.core.utils.Resource
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
    suspend fun getPromptResponse(prompts: List<Message>): Resource<PromptResponse> {
        return try {



            val body = RequestBody(
                messages = prompts
            )

            val call = api.getPromptResponse(body)
            val botResponse = call.execute()

            if (!botResponse.isSuccessful) {
                return Resource.Error("Connection error, check your internet connection and try again.")
            }

            Resource.Success(botResponse.body()!!)


            /*

            //For testing purposes, you can use the code below to simulate a response from the API.

            delay(2500)
            Resource.Success(
                data = PromptResponse(
                    listOf(
                        Choice(
                            finishReason = "stop",
                            index = 0,
                            message = Message(content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras in enim rutrum, porta elit a, maximus turpis. Sed porta magna sed purus gravida pharetra. Vivamus ut eleifend velit. Vivamus purus diam, aliquam scelerisque metus a, convallis blandit est. In elementum tempor mauris, eget lacinia massa ullamcorper eu. Nam at ante quis orci bibendum ornare. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus placerat tempor sem, et posuere purus faucibus in. Morbi pretium ex vel sagittis sollicitudin. Sed commodo lorem et ipsum semper aliquam. Etiam hendrerit lobortis ipsum. Vestibulum id elit dignissim, porttitor arcu id, consectetur lectus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec quis neque a felis fringilla hendrerit id sed turpis. Vivamus fringilla arcu consectetur libero dapibus, in accumsan risus imperdiet.\n" +
                                    "\n" +
                                    "Aenean eget felis nec nibh commodo tempus eu ut neque. Morbi sed imperdiet metus. Vivamus augue urna, malesuada pulvinar eros at, molestie venenatis nisl. Suspendisse pulvinar nibh in purus porta, quis commodo elit molestie. Fusce massa velit, semper nec pretium quis, pellentesque nec purus. Aliquam erat volutpat. Sed lacus enim, bibendum et pharetra mattis, venenatis eu odio.")
                        )
                    )
                )
            )

            */

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }
}