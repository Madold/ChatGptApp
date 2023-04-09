package com.markusw.chatgptapp.domain

import com.markusw.chatgptapp.core.utils.Resource

data class ValidationResult(
    val success: Boolean,
    val message: String? = null
)
