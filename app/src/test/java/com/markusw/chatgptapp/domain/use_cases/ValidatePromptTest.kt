package com.markusw.chatgptapp.domain.use_cases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ValidatePromptTest {

    private lateinit var validatePrompt: ValidatePrompt

    @Before
    fun setup() {
        validatePrompt = ValidatePrompt()
    }

    @Test
    fun `Empty text should not be valid`() {
        val prompt = ""
        val result = validatePrompt(prompt)

        assertEquals(result.success, false)
    }

    @Test
    fun `Prompt that is only blank spaces should not be valid`() {
        val prompt = "              "
        val result = validatePrompt(prompt)

        assertEquals(result.success, false)
    }

}