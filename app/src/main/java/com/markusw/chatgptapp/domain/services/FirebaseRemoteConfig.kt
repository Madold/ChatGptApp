package com.markusw.chatgptapp.domain.services

import com.markusw.chatgptapp.core.common.FirebaseClient
import com.markusw.chatgptapp.core.common.GlobalConfig
import com.orhanobut.logger.Logger

class FirebaseRemoteConfig : RemoteConfigService {

    private val firebaseClient = FirebaseClient
    companion object {
        const val API_KEY = "api_key"
    }
    override suspend fun fetchApiKey() {
        try {
            firebaseClient.remoteConfig.fetchAndActivate()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val apiKey = firebaseClient.remoteConfig.getString(API_KEY)
                        Logger.d("Fetched api key from firebase: $apiKey")
                        GlobalConfig.apiKey = apiKey
                    }
                }
        } catch (e: Exception) {
            Logger.e("Failed to fetch api key from firebase. Exception: ${e.localizedMessage}")
        }
    }
}