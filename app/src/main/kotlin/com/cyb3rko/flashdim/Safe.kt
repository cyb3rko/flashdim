package com.cyb3rko.flashdim

import android.content.Context
import androidx.preference.PreferenceManager

internal object Safe {
    internal const val APPSTART_FLASH = "appstart_flash"
    internal const val BUTTON_VIBRATION = "button_vibration"
    internal const val INITIAL_LEVEL = "initial_level"
    internal const val MAX_LEVEL = "max_level"
    internal const val MORSE_VIBRATION = "morse_vibration"
    internal const val MULTILEVEL = "multilevel"

    internal fun getBoolean(
        context: Context,
        label: String,
        default: Boolean
    ) = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(label, default)

    internal fun getInt(
        context: Context,
        label: String,
        default: Int
    ) = PreferenceManager.getDefaultSharedPreferences(context).getInt(label, default)

    internal fun writeInt(
        context: Context,
        label: String,
        value: Int
    ) = PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(label, value).apply()

    internal fun writeBoolean(
        context: Context,
        label: String,
        value: Boolean
    ) = PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(label, value)
            .apply()
}
