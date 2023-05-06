/*
 * Copyright (c) 2023 Cyb3rKo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyb3rko.flashdim.utils

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
