package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.core.utils.Resource
import com.markusw.chatgptapp.data.network.remote.responses.PromptResponse
import com.markusw.chatgptapp.data.repositories.AndroidChatRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetChatResponseTest {

    @RelaxedMockK
    private lateinit var chatRepository: AndroidChatRepository
    private lateinit var getChatResponse: GetChatResponse

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getChatResponse = GetChatResponse(chatRepository)
    }

    @Test
    fun `getChatResponse() returns Resource of type Success`() = runBlocking {
        // Given
        coEvery { chatRepository.getPromptResponse(any()) } returns Resource.Success(
            PromptResponse(
                choices = listOf()
            )
        )
        // When
          val result = getChatResponse(
              prompts = listOf()
          )
        // Then
        coVerify(exactly = 1) { chatRepository.getPromptResponse(any()) }
        assert(result is Resource.Success)
    }

    @Test
    fun `getChatResponse() returns Resource of type Error`() = runBlocking {
        // Given
        coEvery { chatRepository.getPromptResponse(any()) } returns Resource.Error(
            message = "Error"
        )
        // When
          val result = getChatResponse(
              prompts = listOf()
          )
        // Then
        coVerify(exactly = 1) { chatRepository.getPromptResponse(any()) }
        assert(result is Resource.Error)
    }

}