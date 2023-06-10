package com.markusw.chatgptapp.core.common

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

object FirebaseClient {
    val remoteConfig get() = Firebase.remoteConfig
}