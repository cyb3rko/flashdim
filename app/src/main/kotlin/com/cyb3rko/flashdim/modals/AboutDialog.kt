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
import android.os.Build
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.utils.openUrl
import com.cyb3rko.flashdim.utils.showDialog

internal object AboutDialog {
    fun show(context: Context, buildInfo: BuildInfo) {
        context.showDialog(
            title = context.getString(R.string.dialog_about_title),
            message = context.getString(
                R.string.dialog_about_message,
                buildInfo.versionName,
                buildInfo.versionCode,
                buildInfo.buildType,
                Build.MANUFACTURER,
                Build.MODEL,
                Build.DEVICE,
                getAndroidVersionName(),
                Build.VERSION.SDK_INT
            ),
            icon = R.drawable._ic_information,
            action = { showIconCreditsDialog(context) },
            actionMessage = context.getString(R.string.dialog_about_button)
        )
    }

    private fun showIconCreditsDialog(context: Context) {
        context.showDialog(
            title = context.getString(R.string.dialog_credits_title),
            message = context.getString(R.string.dialog_credits_message),
            icon = R.drawable._ic_information,
            action = { context.openUrl("https://flaticon.com", "Flaticon") },
            actionMessage = context.getString(R.string.dialog_credits_button)
        )
    }

    private fun getAndroidVersionName() = when (Build.VERSION.SDK_INT) {
        33 -> "13"
        34 -> "14"
        35 -> "15"
        else -> "> 15"
    }
}

internal data class BuildInfo(
    internal val versionName: String,
    internal val versionCode: Int,
    internal val buildType: String
)
