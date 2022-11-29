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
        super.onClick()
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.registerTorchCallback(object: TorchCallback() {
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                super.onTorchModeChanged(cameraId, enabled)
                qsTile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                qsTile.updateTile()
            }
        }, Handler(Looper.getMainLooper()))

        when (qsTile.state) {
            Tile.STATE_INACTIVE -> cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
            Tile.STATE_ACTIVE -> cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
        }
    }
}
