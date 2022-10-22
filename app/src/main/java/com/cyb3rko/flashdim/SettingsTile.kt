package com.cyb3rko.flashdim

import android.service.quicksettings.TileService

internal const val SETTINGS_TILE_CLICKED = "settings_tile_clicked"

class SettingsTile : TileService() {
    override fun onClick() {
        super.onClick()
        qsTile?.let {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.let {
                intent.putExtra(SETTINGS_TILE_CLICKED, true)
                startActivityAndCollapse(intent)
            }
        }
    }
}
