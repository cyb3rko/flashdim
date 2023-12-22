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

import kotlinx.coroutines.delay

internal class MorseHandler(
    private val onBlink: (
        letter: Char,
        code: String,
        delay: Long,
        on: Boolean
    ) -> Boolean
) {
    companion object {
        private const val TIME_UNIT = 200L
        private const val DELAY_DOT = TIME_UNIT
        private const val DELAY_DASH = TIME_UNIT * 3
        private const val DELAY_INTRA_CHARACTER = TIME_UNIT
        private const val DELAY_INTER_CHARACTER = TIME_UNIT * 3
        private const val DELAY_INTER_WORD = TIME_UNIT * 7

        private val CHARACTERS = mapOf(
            'A' to ".-",
            'B' to "-...",
            'C' to "-.-.",
            'D' to "-..",
            'E' to ".",
            'F' to "..-.",
            'G' to "--.",
            'H' to "....",
            'I' to "..",
            'J' to ".---",
            'K' to "-.-",
            'L' to ".-..",
            'M' to "--",
            'N' to "-.",
            'O' to "---",
            'P' to ".--.",
            'Q' to "--.-",
            'R' to ".-.",
            'S' to "...",
            'T' to "-",
            'U' to "..-",
            'V' to "...-",
            'W' to ".--",
            'X' to "-..-",
            'Y' to "-.--",
            'Z' to "--..",
            '1' to ".----",
            '2' to "..---",
            '3' to "...--",
            '4' to "....-",
            '5' to ".....",
            '6' to "-....",
            '7' to "--...",
            '8' to "---..",
            '9' to "----.",
            '0' to "-----"
        )
    }

    internal suspend fun flashMessage(message: String) {
        var code: String
        var delay: Long
        var firstLetter = true
        var firstCodePart = true
        message.uppercase().forEach { char ->
            if (char != ' ') {
                if (!firstLetter) delay(DELAY_INTER_CHARACTER)
                code = CHARACTERS[char]!!
                code.forEach { symbol ->
                    if (!firstCodePart) delay(DELAY_INTRA_CHARACTER)
                    delay = when (symbol) {
                        '.' -> DELAY_DOT
                        '-' -> DELAY_DASH
                        else -> 0L
                    }
                    firstCodePart = false
                    val allowedContinuation = blink(char, code, delay)
                    if (!allowedContinuation) return
                }
                firstLetter = false
            } else {
                delay(DELAY_INTER_WORD)
            }
        }
    }

    private suspend fun blink(char: Char, code: String, delay: Long): Boolean {
        onBlink(char, code, delay, true)
        delay(delay)
        return onBlink(char, code, delay, false)
    }

    internal suspend fun waitForRepeat() {
        delay(DELAY_INTER_WORD)
    }
}
