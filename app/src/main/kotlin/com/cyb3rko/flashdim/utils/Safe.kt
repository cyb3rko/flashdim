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
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.cyb3rko.flashdim.BuildConfig

internal object Safe {
    const val APPOPEN_FLASH = "appopen_flash"
    const val APPSTART_FLASH = "appstart_flash"
    const val BUTTON_VIBRATION = "button_vibration"
    const val FLASH_ACTIVE = "flash_active"
    const val INITIAL_LEVEL = "initial_level"
    const val MAX_LEVEL = "max_level"
    const val MORSE_VIBRATION = "morse_vibration"
    const val MULTILEVEL = "multilevel"
    const val PAUSE_FLASH = "pause_flash"
    const val QUICKTILE_DIM_MODE = "dim_mode"
    const val QUICK_SETTINGS_LINK = "quick_settings_link"
    const val REPORT_DIALOG_SHOWN = "${BuildConfig.VERSION_CODE}-report_dialog"
    const val STARTUP_COUNTER = "startup_counter"
    const val VOLUME_BUTTONS_LINK = "volume_buttons_link"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun initialize(context: Context) {
        if (!this::sharedPreferences.isInitialized) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            editor = sharedPreferences.edit()
        }
    }

    fun getBoolean(label: String, default: Boolean) = sharedPreferences.getBoolean(label, default)

    fun getInt(label: String, default: Int) = sharedPreferences.getInt(label, default)

    fun writeInt(label: String, value: Int) = editor.putInt(label, value).apply()

    fun writeBoolean(label: String, value: Boolean) = editor.putBoolean(label, value).apply()
}
