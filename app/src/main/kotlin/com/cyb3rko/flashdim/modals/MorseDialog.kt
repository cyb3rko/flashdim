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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.cyb3rko.flashdim.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.R as MaterialR

internal object MorseDialog {
    fun show(context: Context, onFlash: (message: String) -> Unit) {
        val layoutInflater = (context as FragmentActivity).layoutInflater

        val inputLayout = layoutInflater.inflate(R.layout.dialog_morse_input, null)
            .findViewById<TextInputLayout>(R.id.text_input_layout)

        val inputText = inputLayout.findViewById<TextInputEditText>(R.id.text_input_text)

        MaterialAlertDialogBuilder(
            context,
            MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setIcon(R.drawable._ic_morse)
            .setView(inputLayout)
            .setTitle(R.string.dialog_morse_title)
            .setPositiveButton(R.string.dialog_morse_button, null)
            .create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val message = inputText.text.toString().trim()
                        val error: String
                        if (message.isEmpty()) {
                            error = context.getString(R.string.dialog_morse_error_empty)
                            inputLayout.error = error
                        } else if (message.length > 50) {
                            error = context.getString(R.string.dialog_morse_error_length)
                            inputLayout.error = error
                        } else if (!Regex("[a-zA-Z\\d ]+").matches(message)) {
                            error = context.getString(R.string.dialog_morse_error_characters)
                            inputLayout.error = error
                        } else {
                            dismiss()
                            onFlash(message)
                        }
                    }
                }
            }.show()
    }
}
