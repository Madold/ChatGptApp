package com.markusw.chatgptapp.domain.use_cases

import com.markusw.chatgptapp.domain.services.RemoteConfigService
import javax.inject.Inject

class FetchApiKey @Inject constructor(
    private val remoteConfigService: RemoteConfigService,
) {
    suspend operator fun invoke() = remoteConfigService.fetchApiKey()

}