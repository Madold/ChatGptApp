package com.markusw.chatgptapp

import android.app.Application
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.markusw.chatgptapp.core.common.FirebaseClient
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatGptApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        setupFirebase()
    }

    private fun setupFirebase() {
        val remoteConfigSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
        val firebaseConfig = FirebaseClient.remoteConfig
        firebaseConfig.setConfigSettingsAsync(remoteConfigSettings)
    }

}