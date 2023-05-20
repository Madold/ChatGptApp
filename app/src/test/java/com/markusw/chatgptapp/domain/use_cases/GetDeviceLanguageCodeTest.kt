package com.markusw.chatgptapp.domain.use_cases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetDeviceLanguageCodeTest {

    private lateinit var getDeviceLanguageCode: GetDeviceLanguageCode

    @Before
    fun setup() {
        getDeviceLanguageCode = GetDeviceLanguageCode()
    }

    @Test
    fun `Language code should be only 2 letters of length`() {
        val expectedLength = 2
        val languageCode = getDeviceLanguageCode()

        assertEquals(languageCode.length, expectedLength)
    }

}