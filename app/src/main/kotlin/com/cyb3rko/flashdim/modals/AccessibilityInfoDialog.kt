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

package com.cyb3rko.flashdim.modals

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.cyb3rko.flashdim.R
import com.google.android.material.R as MaterialR
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// included in buildType "debug", "release"
// excluded in buildType "libre"
internal object AccessibilityInfoDialog {
    fun show(context: Context) {
        MaterialAlertDialogBuilder(
            context,
            MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setIcon(R.drawable._ic_information)
            .setTitle(R.string.dialog_accessibility_title)
            .setMessage(R.string.dialog_accessibility_message)
            .setPositiveButton(R.string.dialog_accessibility_button1) { _, _ ->
                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .setNegativeButton(R.string.dialog_accessibility_button2, null)
            .create()
            .show()
    }
}
