package com.markusw.chatgptapp.ui.view.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.markusw.chatgptapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isSplashScreenVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { isSplashScreenVisible }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isSplashScreenVisible = false
    }
}