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

package com.cyb3rko.flashdim.tiles

import android.content.Context
import android.hardware.camera2.CameraManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.cyb3rko.flashdim.handleFlashlightException
import com.cyb3rko.flashdim.utils.Safe

class DimmerSettingsTile : TileService() {
    override fun onClick() {
        if (qsTile.state == Tile.STATE_UNAVAILABLE) return
        Safe.initialize(applicationContext)
        val mode = Safe.getInt(Safe.QUICKTILE_DIM_MODE, DIMMER_MIN)

        val maxLevel = Safe.getInt(Safe.MAX_LEVEL, -1)
        val newLevel = when (mode) {
            DIMMER_MAX -> maxLevel
            DIMMER_HALF -> maxLevel / 2
            DIMMER_MIN -> 1
            DIMMER_OFF -> 0
            else -> DIMMER_MAX
        }

        try {
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            sendFlashlightSignal(cameraManager, newLevel, newLevel != DIMMER_OFF)
        } catch (e: Exception) {
            Log.e("FlashDim", "Camera access failed in DimmerSettingsTile")
            handleFlashlightException(e)
            e.printStackTrace()
        }
        qsTile.subtitle = "State: ${mode.description()}"
        qsTile.updateTile()
        Safe.writeInt(Safe.QUICKTILE_DIM_MODE, mode.next())
    }

    override fun onStartListening() {
        Safe.initialize(this)
        if (Safe.getInt(Safe.MAX_LEVEL, -1) < 2) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            qsTile.updateTile()
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
        }
    }

    private fun sendFlashlightSignal(
        cameraManager: CameraManager,
        level: Int,
        activate: Boolean
    ) {
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
