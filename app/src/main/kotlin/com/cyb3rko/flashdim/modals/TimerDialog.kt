/*
 * Copyright (c) 2022-2025 Cyb3rKo
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.databinding.DialogTimerBinding
import com.cyb3rko.flashdim.service.TimerService
import com.cyb3rko.flashdim.utils.disable
import com.cyb3rko.flashdim.utils.enable
import com.google.android.material.R as MaterialR
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal object TimerDialog {
    private const val MIN_MINUTES = 1
    private const val MAX_MINUTES = 60

    private var nakedBinding: DialogTimerBinding? = null

    // This property is only valid while the dialog is shown.
    private val binding get() = nakedBinding!!
    private var dialog: AlertDialog? = null

    /**
     * Shows the timer dialog. Closing it does *not* cancel a running timer, therefore the state
     * of the buttons is always derived from [TimerService].
     *
     * @param onStart called when a timer is started, the caller switches the light on
     */
    fun show(context: Context, onStart: () -> Unit) {
        nakedBinding = DialogTimerBinding.inflate((context as FragmentActivity).layoutInflater)
        binding.numberInputLayout.hint = context.getString(
            R.string.dialog_timer_input_hint,
            MIN_MINUTES,
            MAX_MINUTES
        )
        binding.startButton.setOnClickListener {
            val minutes = binding.numberInputText.text.toString().toIntOrNull()
            if (minutes == null || minutes !in MIN_MINUTES..MAX_MINUTES) {
                binding.numberInputLayout.error = context.getString(
                    R.string.dialog_timer_error,
                    MIN_MINUTES,
                    MAX_MINUTES
                )
                return@setOnClickListener
            }
            binding.numberInputLayout.error = null

            onStart()
            TimerService.start(context, minutes)
            showRunningState(true)
        }
        binding.stopButton.setOnClickListener {
            // The service switches the light off and reports back via TimerService.onFinished,
            // which is what actually updates the UI (see MainActivity)
            TimerService.stop(context)
        }
        showRunningState(TimerService.isRunning)

        dialog = MaterialAlertDialogBuilder(
            context,
            MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setIcon(R.drawable.ic_timer)
            .setTitle(R.string.dialog_timer_title)
            .setView(binding.root)
            .setOnDismissListener { nakedBinding = null }
            .create()
        dialog?.show()
    }

    fun stop() {
        dialog?.dismiss()
        dialog = null
        nakedBinding = null
    }

    // Resets the buttons in case the dialog is still open when the timer ends
    fun onTimerFinished() = showRunningState(false)

    private fun showRunningState(running: Boolean) {
        val binding = nakedBinding ?: return
        binding.numberInputText.isEnabled = !running
        if (running) {
            binding.startButton.disable()
            binding.stopButton.enable()
        } else {
            binding.startButton.enable()
            binding.stopButton.disable()
        }
    }
}
