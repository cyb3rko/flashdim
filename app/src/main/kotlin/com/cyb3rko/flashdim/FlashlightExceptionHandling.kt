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

import android.hardware.camera2.CameraAccessException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal fun handleFlashlightException(exception: Exception, activity: AppCompatActivity? = null) {
    if (exception is CameraAccessException) {
        val message = when (exception.reason) {
            CameraAccessException.CAMERA_DISABLED -> {
                "The camera service is disabled"
            }
            CameraAccessException.CAMERA_DISCONNECTED -> {
                "The camera service is disconnected"
            }
            CameraAccessException.CAMERA_ERROR -> {
                "The camera service is in error state"
            }
            CameraAccessException.CAMERA_IN_USE -> {
                "The camera service is already in use"
            }
            CameraAccessException.MAX_CAMERAS_IN_USE -> {
                "The limit of open camera services is reached"
            }
            else -> {
                "Unknown camera access error"
            }
        }
        Log.e("FlashDimError", message)
        if (activity != null) showErrorDialog(activity, message)
        return
    } else if (exception is IllegalArgumentException) {
        val message = "CameraManager received illegal arguments, probably empty camera id"
        Log.e("FlashDimError", message)
        if (activity != null) showErrorDialog(activity, message)
        return
    }

    throw exception
}

internal fun showErrorDialog(
    activity: AppCompatActivity,
    message: String,
    onClick: () -> Unit = {}
) {
    MaterialAlertDialogBuilder(activity)
        .setTitle("Exception occured")
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("Roger") { _, _ ->
            onClick()
        }
        .show()
}
