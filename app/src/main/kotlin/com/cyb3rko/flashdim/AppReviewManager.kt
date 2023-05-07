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

package com.cyb3rko.flashdim

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.cyb3rko.flashdim.utils.Safe
import com.cyb3rko.flashdim.utils.showDialog

internal object AppReviewManager {
    fun initiateReviewDialog(context: Context) {
        val count = Safe.getInt(context, Safe.STARTUP_COUNTER, 0)
        if (count > 15) return
        if (count == 5 || count == 15) {
            val n = if (count == 5) 1 else 2
            context.showDialog(
                title = "Do you like the app?",
                message = "This app is ad-free and free to use.\n" +
                        "Please consider rating the app if you like it.\n\n" +
                        "($n/2 reminders)",
                icon = android.R.drawable.btn_star,
                action = {
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "https://play.google.com/store/apps/details?id=com.cyb3rko.flashdim"
                                )
                            )
                        )
                    } catch (_: Exception) {}
                },
                actionMessage = "Rate"
            )
        }
        increaseCounter(context, count)
    }

    private fun increaseCounter(context: Context, prevCount: Int) {
        Safe.writeInt(context, Safe.STARTUP_COUNTER, prevCount + 1)
    }
}
