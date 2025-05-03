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

package com.cyb3rko.flashdim.quicksettings

import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.TorchCallback
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.cyb3rko.flashdim.handleFlashlightException
import com.cyb3rko.flashdim.utils.Safe

class SettingsTile : TileService() {
    private var cameraManager: CameraManager? = null
    private var description = ""
    private var enabled = false

    override fun onStartListening() {
        super.onStartListening()
        if (qsTile == null) {
            Log.e("FlashDim", "SettingsTile null, exiting now")
        }
        Log.d("FlashDim", "Initializing SettingsTile")
        Safe.initialize(this)
        try {
            cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            val toggleMode = Safe.getBoolean(Safe.QUICKTILE_TOGGLE_MODE, true)
            if (toggleMode) {
                initAsToggle()
            } else {
                initAsDimmer()
            }
        } catch (e: Exception) {
            Log.e("FlashDim", "Initializing SettingsTile failed")
            e.printStackTrace()
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        cameraManager = null
    }

    override fun onClick() {
        if (qsTile.state == Tile.STATE_UNAVAILABLE) return

        var toggleMode = false
        try {
            Safe.initialize(applicationContext)
            toggleMode = Safe.getBoolean(Safe.QUICKTILE_TOGGLE_MODE, true)
        } catch (_: Exception) {
            Log.e("FlashDim", "Safe operations failed in SettingsTile")
        }

        if (toggleMode) {
            actAsToggle()
        } else {
            actAsDimmer()
        }
    }

    private fun initAsToggle() {
        Log.d("FlashDim", "Initializing SettingsTile (toggle)")
        cameraManager?.registerTorchCallback(
            object : TorchCallback() {
                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    if (qsTile == null) return
                    Safe.initialize(applicationContext)
                    Safe.writeBoolean(Safe.FLASH_ACTIVE, enabled)
                    this@SettingsTile.enabled = enabled
                    qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                    qsTile.updateTile()
                }
            },
            Handler(Looper.getMainLooper())
        )
    }

    private fun initAsDimmer() {
        Log.d("FlashDim", "Initializing SettingsTile (dimmer)")
        if (Safe.getInt(Safe.MAX_LEVEL, -1) < 2) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            qsTile.updateTile()
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
            cameraManager?.registerTorchCallback(
                object : TorchCallback() {
                    override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                        if (qsTile == null) return
                        this@SettingsTile.enabled = enabled
                        qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                        qsTile.updateTile()
                    }
                },
                Handler(Looper.getMainLooper())
            )
        }
    }

    private fun actAsToggle() {
        var level = -1
        try {
            if (Safe.getBoolean(Safe.QUICK_SETTINGS_LINK, false)) {
                level = Safe.getInt(Safe.INITIAL_LEVEL, 1)
            }
        } catch (_: Exception) {
            Log.e("FlashDim", "Safe operations failed in SettingsTile")
        }

        qsTile.subtitle = null
        try {
            if (cameraManager == null) {
                Log.d("FlashDim", "Initializing CameraManager from SettingsTile (toggle)")
                cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            }
            cameraManager?.let {
                Log.d("FlashDim", "Toggling flashlight from SettingsTile (toggle)")
                when (qsTile.state) {
                    Tile.STATE_INACTIVE -> sendFlashlightSignal(it, level, true)
                    Tile.STATE_ACTIVE -> sendFlashlightSignal(it, level, false)
                }
            }
        } catch (e: Exception) {
            Log.e("FlashDim", "Camera access failed in SettingsFilel (toggle)")
            handleFlashlightException(e)
            e.printStackTrace()
        }
    }

    private fun actAsDimmer() {
        var stage: Int
        if (!enabled) {
            // torch was disabled externally, continue again from initial state
            stage = DIMMER_MIN
        } else {
            try {
                stage = Safe.getInt(Safe.QUICKTILE_DIM_STAGE, DIMMER_MIN)
            } catch (_: Exception) {
                Log.e("FlashDim", "Safe operations failed in SettingsTile")
                stage = DIMMER_MIN
            }
        }
        description = stage.description()

        val maxLevel = Safe.getInt(Safe.MAX_LEVEL, -1)
        val newLevel = when (stage) {
            DIMMER_MAX -> maxLevel
            DIMMER_HALF -> maxLevel / 2
            DIMMER_MIN -> 1
            DIMMER_OFF -> 0
            else -> DIMMER_MAX
        }

        try {
            if (cameraManager == null) {
                Log.d("FlashDim", "Initializing CameraManager from SettingsTile (dimmer)")
                cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            }
            cameraManager?.let {
                Log.d("FlashDim", "Dimming flashlight from SettingsTile (dimmer)")
                sendFlashlightSignal(it, newLevel, newLevel != DIMMER_OFF)
            }
        } catch (e: Exception) {
            Log.e("FlashDim", "Camera access failed in SettingsTile (dimmer")
            handleFlashlightException(e)
            e.printStackTrace()
        }
        qsTile.subtitle = if (!enabled) {
            "State: ${DIMMER_MIN.description()}"
        } else {
            "State: $description"
        }
        qsTile.updateTile()
        Safe.writeInt(Safe.QUICKTILE_DIM_STAGE, stage.next())
    }

    private fun sendFlashlightSignal(cameraManager: CameraManager, level: Int, activate: Boolean) {
        if (activate) {
            if (level == -1) {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
            } else {
                cameraManager.turnOnTorchWithStrengthLevel(cameraManager.cameraIdList[0], level)
            }
        } else {
            cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
        }
    }

    private fun Int.next() = when (this) {
        DIMMER_OFF -> DIMMER_MIN
        DIMMER_MIN -> DIMMER_HALF
        DIMMER_HALF -> DIMMER_MAX
        DIMMER_MAX -> DIMMER_OFF
        else -> DIMMER_MAX
    }

    private fun Int.description() = when (this) {
        DIMMER_OFF -> "Off"
        DIMMER_MIN -> "Min"
        DIMMER_HALF -> "Half"
        DIMMER_MAX -> "Max"
        else -> "Unknown"
    }

    companion object {
        const val DIMMER_MAX = 3
        const val DIMMER_HALF = 2
        const val DIMMER_MIN = 1
        const val DIMMER_OFF = 0
    }
}
