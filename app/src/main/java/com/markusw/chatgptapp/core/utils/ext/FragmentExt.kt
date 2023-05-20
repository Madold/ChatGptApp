package com.markusw.chatgptapp.core.utils.ext

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment

fun Fragment.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", requireContext().packageName, this::class.java.simpleName)
    ).also(::startActivity)
}