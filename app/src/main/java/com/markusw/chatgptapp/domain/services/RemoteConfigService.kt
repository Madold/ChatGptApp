package com.markusw.chatgptapp.domain.services

interface RemoteConfigService {
    suspend fun fetchApiKey()
}