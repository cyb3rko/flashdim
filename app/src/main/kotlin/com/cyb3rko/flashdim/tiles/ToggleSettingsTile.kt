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

package com.cyb3rko.flashdim.tiles

import android.content.Context
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.TorchCallback
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.cyb3rko.flashdim.handleFlashlightException
import com.cyb3rko.flashdim.utils.Safe

class ToggleSettingsTile : TileService() {
    private var cameraManager: CameraManager? = null

    override fun onClick() {
        if (qsTile.state == Tile.STATE_UNAVAILABLE) return

        var level = -1
        try {
            Safe.initialize(applicationContext)
            if (Safe.getBoolean(Safe.QUICK_SETTINGS_LINK, false)) {
                level = Safe.getInt(Safe.INITIAL_LEVEL, 1)
            }
        } catch (e: Exception) {
            Log.e("FlashDim", "Safe operations failed in ToggleSettingsTile")
        }

        try {
            if (cameraManager == null) {
                Log.d("FlashDim", "Initializing CameraManager from ToggleSettingsTile")
                cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            }
            cameraManager?.let {
                Log.d("FlashDim", "Toggling flashlight from ToggleSettingsTile")
                when (qsTile.state) {
                    Tile.STATE_INACTIVE -> sendFlashlightSignal(it, level, true)
                    Tile.STATE_ACTIVE -> sendFlashlightSignal(it, level, false)
                }
            }
        } catch (e: Exception) {
            Log.e("FlashDim", "Camera access failed in ToggleSettingsTile")
            handleFlashlightException(e)
            e.printStackTrace()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        if (qsTile == null) {
            Log.e("FlashDim", "ToggleSettingsTile null, exiting now")
        }
        Log.d("FlashDim", "Initializing ToggleSettingsTile")
        try {
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraManager?.registerTorchCallback(
                object : TorchCallback() {
                    override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                        if (qsTile == null) return
                        Safe.initialize(applicationContext)
                        Safe.writeBoolean(Safe.FLASH_ACTIVE, enabled)
                        qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                        qsTile.updateTile()
                    }
                },
                Handler(Looper.getMainLooper())
            )
        } catch (e: Exception) {
            Log.e("FlashDim", "Initializing ToggleSettingsTile failed")
            e.printStackTrace()
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        cameraManager = null
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
}
