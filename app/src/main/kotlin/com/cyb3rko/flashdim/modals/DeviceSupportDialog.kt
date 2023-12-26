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

package com.cyb3rko.flashdim.modals

import android.content.Context
import androidx.annotation.StringRes
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.utils.Safe
import com.cyb3rko.flashdim.utils.openUrl
import com.cyb3rko.flashdim.utils.storeToClipboard
import com.google.android.material.R as MaterialR
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal object DeviceSupportDialog {
    fun show(context: Context, maxLevel: Int, force: Boolean) {
        if (wasAlreadyShown() && !force) return else saveAlreadyShown()
        showDialog(context, maxLevel, R.string.dialog_device_support_message2)
    }

    fun showWrong(context: Context, maxLevel: Int, force: Boolean) {
        if (wasAlreadyShown() && !force) return else saveAlreadyShown()
        showDialog(context, maxLevel, R.string.dialog_device_support_message1)
    }

    private fun showDialog(context: Context, maxLevel: Int, @StringRes message: Int) {
        MaterialAlertDialogBuilder(
            context,
            MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setTitle(R.string.dialog_device_support_title)
            .setMessage(message)
            .setIcon(R.drawable._ic_information)
            .setPositiveButton(R.string.dialog_device_support_button1_2) { _, _ ->
                val deviceInfo = AboutDialog.getDeviceInfo(maxLevel)
                context.storeToClipboard("System Info FlashDim", deviceInfo)
                context.openUrl(
                    "https://github.com/cyb3rko/flashdim/issues",
                    "GitHub Issues"
                )
            }
            .setNeutralButton(R.string.dialog_device_support_button1_1) { _, _ ->
                val deviceInfo = AboutDialog.getDeviceInfo(maxLevel)
                context.storeToClipboard("System Info FlashDim", deviceInfo)
            }
            .show()
    }

    private fun wasAlreadyShown(): Boolean {
        return Safe.getBoolean(Safe.REPORT_DIALOG_SHOWN, false)
    }

    private fun saveAlreadyShown() {
        Safe.writeBoolean(Safe.REPORT_DIALOG_SHOWN, true)
    }
}
