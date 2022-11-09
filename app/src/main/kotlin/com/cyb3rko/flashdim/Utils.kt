package com.cyb3rko.flashdim

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.VibrationEffect
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// For View class

internal fun View.disable() {
    this.isEnabled = false
}

internal fun View.enable() {
    this.isEnabled = true
}

internal fun View.hide() {
    this.visibility = View.GONE
}

internal fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

internal fun View.show() {
    this.visibility = View.VISIBLE
}

// For Context class

internal fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

internal fun Context.showDialog(
    title: String,
    message: CharSequence,
    icon: Int?,
    action: () -> Unit = {},
    actionMessage: String = "",
    cancelable: Boolean = true
) {
    val builder = MaterialAlertDialogBuilder(
        this,
        com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
    )
        .setTitle(title)
        .setMessage(message)
        .setCancelable(cancelable)

    if (icon != null) {
        builder.setIcon(
            ResourcesCompat.getDrawable(resources, icon, theme)
        )
    }

    if (actionMessage.isNotBlank()) {
        builder.setPositiveButton(actionMessage) { _, _ ->
            action()
        }
    }
    builder.show()
}

internal fun Context.storeToClipboard(label: String, text: String) {
    val clip = ClipData.newPlainText(label, text)
    (this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .setPrimaryClip(clip)
}

internal fun Context.openUrl(url: String, label: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        this.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        this.storeToClipboard(label, url)
        this.showToast(getString(R.string.toast_url_failed), Toast.LENGTH_LONG)
    }
}
