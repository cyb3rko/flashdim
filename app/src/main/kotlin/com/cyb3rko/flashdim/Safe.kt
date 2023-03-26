package com.cyb3rko.flashdim

import android.content.Context
import androidx.preference.PreferenceManager

internal object Safe {
    const val APPOPEN_FLASH = "appopen_flash"
    const val APPSTART_FLASH = "appstart_flash"
    const val BUTTON_VIBRATION = "button_vibration"
    const val FLASH_ACTIVE = "flash_active"
    const val INITIAL_LEVEL = "initial_level"
    const val MAX_LEVEL = "max_level"
    const val MORSE_VIBRATION = "morse_vibration"
    const val MULTILEVEL = "multilevel"
    const val QUICK_SETTINGS_LINK = "quick_settings_link"

    fun getBoolean(
        context: Context,
        label: String,
        default: Boolean
    ) = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(label, default)

    fun getInt(
        context: Context,
        label: String,
        default: Int
    ) = PreferenceManager.getDefaultSharedPreferences(context).getInt(label, default)

    fun writeInt(
        context: Context,
        label: String,
        value: Int
    ) = PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(label, value).apply()

    fun writeBoolean(
        context: Context,
        label: String,
        value: Boolean
    ) = PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(label, value)
            .apply()
}
