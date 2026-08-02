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

package com.cyb3rko.flashdim

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import com.cyb3rko.flashdim.utils.Safe

internal fun clampFlashStrengthLevel(level: Int, maximumLevel: Int): Int =
    level.coerceIn(1, maximumLevel)

internal class Camera(activity: AppCompatActivity) {
    private val cameraManager: CameraManager
    private val cameraId: String
    val idEmpty: Boolean
        get() = cameraId.isEmpty()
    val maxLevel: Int
        get() {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            return characteristics[CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL] ?: -1
        }

    init {
        cameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = findFlashCameraId(activity, cameraManager)
    }

    private fun findFlashCameraId(
        activity: AppCompatActivity,
        cameraManager: CameraManager
    ): String {
        val firstCameraWithFlash = cameraManager.cameraIdList.find { camera ->
            cameraManager.getCameraCharacteristics(camera).keys.any { key ->
                key == CameraCharacteristics.FLASH_INFO_AVAILABLE
            }
        }

        if (firstCameraWithFlash != null) {
            return firstCameraWithFlash
        }
        showErrorDialog(activity, "Camera with flashlight not found") {
            activity.finish()
        }
        return ""
    }

    fun setTorchMode(enabled: Boolean) {
        cameraManager.setTorchMode(cameraId, enabled)
    }

    fun sendLightLevel(activity: AppCompatActivity, currentLevel: Int, level: Int) {
        if (currentLevel != level) {
            try {
                cameraManager.turnOnTorchWithStrengthLevel(cameraId, level)
            } catch (e: Exception) {
                handleFlashlightException(e, activity)
            }
        }
    }

    companion object {
        fun doesDeviceHaveFlash(packageManager: PackageManager): Boolean =
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        fun sendLightLevel(context: Context, level: Int, activate: Boolean) {
            try {
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE)
                    as CameraManager
                sendLightLevel(context, cameraManager, level, activate)
            } catch (e: Exception) {
                handleFlashlightException(e)
            }
        }

        fun sendLightLevel(
            context: Context,
            cameraManager: CameraManager,
            level: Int,
            activate: Boolean
        ) {
            val cameraId = cameraManager.cameraIdList[0]
            if (activate) {
                if (level == -1) {
                    cameraManager.setTorchMode(cameraId, true)
                } else {
                    val maximumLevel = cameraManager.getCameraCharacteristics(cameraId).get(
                        CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL
                    )
                    val safeLevel = if (maximumLevel != null && maximumLevel > 0) {
                        clampFlashStrengthLevel(level, maximumLevel)
                    } else {
                        level
                    }
                    if (safeLevel != level) {
                        Safe.initialize(context)
                        Safe.writeInt(Safe.PREFERRED_LEVEL, safeLevel)
                    }
                    cameraManager.turnOnTorchWithStrengthLevel(cameraId, safeLevel)
                }
            } else {
                cameraManager.setTorchMode(cameraId, false)
            }
        }
    }
}
