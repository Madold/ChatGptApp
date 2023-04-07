package com.markusw.chatgptapp.core.utils

import com.google.gson.InstanceCreator
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class CallInstanceCreator<T> : InstanceCreator<Call<T>> {
    override fun createInstance(type: Type): Call<T> {
        return object : Call<T> {
            override fun enqueue(callback: Callback<T>) {
                // no-op
            }

            override fun isExecuted(): Boolean {
                return false
            }

            override fun clone(): Call<T> {
                return this
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun cancel() {
                // no-op
            }

            override fun request(): Request {
                return Request.Builder().build()
            }

            override fun timeout(): Timeout {
                return Timeout()
            }

            override fun execute(): Response<T> {
                throw UnsupportedOperationException()
            }
        }
    }
}