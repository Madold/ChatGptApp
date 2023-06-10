package com.markusw.chatgptapp.data.network.interceptors

import com.markusw.chatgptapp.core.common.GlobalConfig
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ChatGptApiInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        Logger.d("intercepting request with api key: ${GlobalConfig.apiKey}")

        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer ${GlobalConfig.apiKey}")
            .build()

        return chain.proceed(request)
    }
}