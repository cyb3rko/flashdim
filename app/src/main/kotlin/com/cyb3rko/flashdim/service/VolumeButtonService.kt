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

package com.cyb3rko.flashdim.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.cyb3rko.flashdim.Camera
import com.cyb3rko.flashdim.utils.Safe

class VolumeButtonService : AccessibilityService() {
    private var volumeUpPressed = false
    private var volumeDownPressed = false

    override fun onServiceConnected() {
        Safe.initialize(applicationContext)
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.registerTorchCallback(
            object : CameraManager.TorchCallback() {
                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    Safe.writeBoolean(Safe.FLASH_ACTIVE, enabled)
                }
            },
            Handler(Looper.getMainLooper())
        )
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return false
        if ((event.keyCode == KeyEvent.KEYCODE_VOLUME_UP) ||
            (event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        ) {
            Safe.initialize(applicationContext)
            val pressed = event.action == KeyEvent.ACTION_DOWN
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> volumeUpPressed = pressed
                KeyEvent.KEYCODE_VOLUME_DOWN -> volumeDownPressed = pressed
            }
            if (volumeUpPressed && volumeDownPressed) {
                Log.i("FlashDim Service", "Both volume buttons pressed")
                val flashActive = Safe.getBoolean(Safe.FLASH_ACTIVE, false)
                val flashLevel = if (!flashActive) getFlashLevel() else 0
                Camera.sendLightLevel(applicationContext, flashLevel, !flashActive)
            }
        } else {
            volumeUpPressed = false
            volumeDownPressed = false
        }
        return false
    }

    private fun getFlashLevel(): Int = if (Safe.getBoolean(Safe.VOLUME_BUTTONS_LINK, false)) {
        Safe.getInt(Safe.INITIAL_LEVEL, -1)
    } else {
        -1
    }

    override fun onInterrupt() {
        Log.i("FlashDim Service", "VolumeButtonService interrupted")
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {}
}
