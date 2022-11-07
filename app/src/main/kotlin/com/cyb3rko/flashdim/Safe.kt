package com.cyb3rko.flashdim

import android.content.Context
import androidx.preference.PreferenceManager

internal object Safe {
    internal const val APPSTART_FLASH = "appstart_flash"
    internal const val BUTTON_VIBRATION = "button_vibration"
    internal const val MORSE_VIBRATION = "morse_vibration"

    internal fun getBoolean(context: Context, label: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(label, false)
    }
}
