/*
 * Copyright (c) 2022-2024 Cyb3rKo
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

import android.content.res.Resources
import android.os.Build
import com.cyb3rko.flashdim.R

internal object DeviceSupportManager {
    fun isExcluded(resources: Resources): Pair<Boolean, Boolean> {
        val brand = Build.BRAND.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        val device = Build.DEVICE.lowercase()
        var content: Array<String>
        var deviceReportLocked = false
        val deviceExcluded = resources
            .openRawResource(R.raw.excluded_devices)
            .bufferedReader()
            .lines().skip(1).anyMatch {
                // 0: gplay-name
                // 1: gplay-technical-name
                // 2: branding
                // 3: device
                // 4: locked (ignore device brightness levels)
                content = it.split(",").toTypedArray()
                deviceReportLocked = content[4].toBoolean()
                (content[2].lowercase() == manufacturer || content[2].lowercase() == brand) &&
                    content[3].lowercase() == device
            }
        return deviceExcluded to deviceReportLocked
    }
}
