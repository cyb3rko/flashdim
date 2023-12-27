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
import androidx.fragment.app.FragmentActivity
import com.cyb3rko.flashdim.IntervalHandler
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.databinding.DialogIntervalBinding
import com.cyb3rko.flashdim.utils.disable
import com.cyb3rko.flashdim.utils.enable
import com.google.android.material.R as MaterialR
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal object IntervalDialog {
    private lateinit var binding: DialogIntervalBinding
    private var mode = 0
    private var time = "500"
    private var bpm = "100"

    fun show(context: Context, onBlink: (on: Boolean) -> Unit) {
        val intervalHandler = IntervalHandler(onBlink)
        binding = DialogIntervalBinding.inflate((context as FragmentActivity).layoutInflater)
        binding.timeButton.setOnClickListener {
            switchToTime()
        }
        binding.bpmButton.setOnClickListener {
            switchToBpm()
        }
        binding.flashButton.setOnClickListener {
            val input = binding.numberInputText.text.toString()
            if (!validateInput(input)) {
                binding.numberInputLayout.error = context.getString(R.string.dialog_interval_error)
                return@setOnClickListener
            }
            binding.numberInputLayout.error = null

            it.disable()
            binding.stopButton.enable()

            when (mode) {
                0 -> intervalHandler.blinkInterval(input)
                1 -> intervalHandler.blinkBpm(input)
            }
        }
        binding.stopButton.setOnClickListener {
            intervalHandler.stop()
            it.disable()
            binding.flashButton.enable()
        }
        binding.seizureWarning.text = context.getText(R.string.dialog_interval_seizure_warning)

        val dialog = MaterialAlertDialogBuilder(
            context,
            MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setIcon(android.R.drawable.stat_notify_sync)
            .setTitle(R.string.dialog_interval_title)
            .setView(binding.root)
            .create()
        dialog.show()
    }

    private fun switchToTime() {
        bpm = binding.numberInputText.text.toString()
        mode = 0
        binding.numberInputLayout.setHint(R.string.dialog_interval_input_hint1)
        binding.numberInputLayout.suffixText = "ms"
        binding.numberInputLayout.counterMaxLength = 5
        binding.numberInputText.setText(time)
    }

    private fun switchToBpm() {
        time = binding.numberInputText.text.toString()
        mode = 1
        binding.numberInputLayout.setHint(R.string.dialog_interval_input_hint2)
        binding.numberInputLayout.suffixText = null
        binding.numberInputLayout.counterMaxLength = 3
        binding.numberInputText.setText(bpm)
    }

    private fun validateInput(input: String): Boolean {
        return if (input.isEmpty()) {
            false
        } else if (mode == 0) {
            validateTime(input)
        } else {
            validateBpm(input)
        }
    }

    private fun validateTime(time: String) = time.toInt() in 200..10000

    private fun validateBpm(bpm: String) = bpm.toInt() in 10..200
}
