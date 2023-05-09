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

import android.os.Handler
import android.os.Looper
import java.util.Timer
import java.util.TimerTask

internal class IntervalHandler(private val onBlink: (on: Boolean) -> Unit) {
    var running = true

    internal fun blinkInterval(interval: String) {
        blink(interval.toLong())
    }

    internal fun blinkBpm(bpm: String) {
        val time = 60_000 / bpm.toLong()
        blink(time)
    }

    private fun blink(time: Long) {
        running = true
        val timer = Timer(true)
        val handler = Handler(Looper.getMainLooper())
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (!running) {
                    timer.cancel()
                    return
                }
                onBlink(true)
                handler.postDelayed({
                    onBlink(false)
                }, FLASH_DURATION)
            }
        }, 0, time)
    }

    fun stop() {
        running = false
    }

    companion object {
        private const val FLASH_DURATION = 50L
    }
}
