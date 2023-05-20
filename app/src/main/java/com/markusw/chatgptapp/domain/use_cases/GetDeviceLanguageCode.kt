package com.markusw.chatgptapp.domain.use_cases

import java.util.Locale
import javax.inject.Inject

class GetDeviceLanguageCode @Inject constructor() {

    operator fun invoke(): String {
        return Locale.getDefault().toLanguageTag().takeWhile { it != '-' }
    }

}