package com.markusw.chatgptapp.core.utils

sealed class Resource<T>(val data: T? = null, var message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(null, message)
}