package com.markusw.chatgptapp.domain

data class ValidationResult(
    val success: Boolean,
    val message: String? = null
)
