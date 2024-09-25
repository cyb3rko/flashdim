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
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.cyb3rko.flashdim.handleFlashlightException
import com.cyb3rko.flashdim.utils.Safe
import kotlin.math.abs

class DimmerSettingsTile : TileService() {
    private var strength = 0

    override fun onClick() {
        if (qsTile.state == Tile.STATE_UNAVAILABLE) return
        Safe.initialize(applicationContext)

        val maxLevel = Safe.getInt(Safe.MAX_LEVEL, -1)
        val nextStrength = findStrengthLevel(strength, maxLevel, nextLevel = true)

        try {
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            sendFlashlightSignal(cameraManager, nextStrength, nextStrength != 0)
        } catch (e: Exception) {
            Log.e("FlashDim", "Camera access failed in DimmerSettingsTile")
            handleFlashlightException(e)
            e.printStackTrace()
        }
        this.strength = nextStrength
        qsTile.subtitle = getModeDesc(strength, maxLevel)
        qsTile.updateTile()
    }

    override fun onStartListening() {
        Safe.initialize(this)
        val maxLevel = Safe.getInt(Safe.MAX_LEVEL, -1)
        if (Safe.getInt(Safe.MAX_LEVEL, -1) < 2) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            qsTile.updateTile()
        } else {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraManager.registerTorchCallback(
                object : CameraManager.TorchCallback() {
                    override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                        if (qsTile == null) return
                        val strength =
                            if (enabled) cameraManager.getTorchStrengthLevel(cameraId) else 0
                        this@DimmerSettingsTile.strength = strength

                        qsTile.subtitle = getModeDesc(strength, maxLevel)
                        qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                        qsTile.updateTile()
                    }
                },
                Handler(Looper.getMainLooper())
            )
        }
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

    private fun findStrengthLevel(value: Int, maxLevel: Int, nextLevel: Boolean = false): Int {
        val levels = listOf(0, 1, maxLevel / 2, maxLevel)
        // find nearest strength level
        // this avoids cases where a strength level of 2 is mapped to a half (since it is higher than min)
        val nearestValue = levels.minByOrNull { abs(it - value) } ?: 0
        // find the next higher strength or 0
        val comparisonOp: (Int, Int) -> Boolean = if (nextLevel) {
            { a, b -> a > b }
        } else {
            { a, b -> a >= b }
        }
        return levels.iterator().asSequence().firstOrNull { comparisonOp(it, nearestValue) } ?: 0
    }

    private fun getModeDesc(value: Int, maxLevel: Int): String {
        return "State: ${when (findStrengthLevel(value, maxLevel)) {
            0 -> "Off"
            1 -> "Min"
            maxLevel / 2 -> "Half"
            maxLevel -> "Max"
            else -> "Unknown"
        }}"
    }
}
