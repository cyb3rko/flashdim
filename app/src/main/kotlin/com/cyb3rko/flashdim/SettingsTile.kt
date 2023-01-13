package com.cyb3rko.flashdim

import android.content.Context
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.TorchCallback
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class SettingsTile : TileService() {
    override fun onClick() {
        var level = -1
        if (Safe.getBoolean(applicationContext, Safe.QUICK_SETTINGS_LINK, false)) {
            level = Safe.getInt(applicationContext, Safe.INITIAL_LEVEL, 1)
        }

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> sendFlashlightSignal(cameraManager, level, true)
            Tile.STATE_ACTIVE -> sendFlashlightSignal(cameraManager, level, false)
        }
    }

    override fun onStartListening() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.registerTorchCallback(object: TorchCallback() {
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                qsTile.updateTile()
            }
        }, Handler(Looper.getMainLooper()))
    }

    private fun sendFlashlightSignal(
        cameraManager: CameraManager,
        level: Int,
        activated: Boolean
    ) {
        try {
            if (!activated) {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
            } else {
                if (level == -1) {
                    cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                } else {
                    cameraManager.turnOnTorchWithStrengthLevel(cameraManager.cameraIdList[0], level)
                }
            }
        } catch (e: Exception) {
            handleFlashlightException(e)
        }
    }
}
