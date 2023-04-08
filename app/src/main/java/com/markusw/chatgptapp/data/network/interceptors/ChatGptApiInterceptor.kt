package com.markusw.chatgptapp.data.network.interceptors

import com.markusw.chatgptapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ChatGptApiInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", " application/json")
            .addHeader("Authorization", " Bearer ${BuildConfig.API_KEY}")
            .build()

        return chain.proceed(request)
    }
}