package com.markusw.chatgptapp.core.utils.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

suspend fun <T> Flow<T>.collectLatestWithoutSubscribe(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)): StateFlow<T> {
    return this.stateIn(scope)
}