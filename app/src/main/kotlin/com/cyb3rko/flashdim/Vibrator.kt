package com.cyb3rko.flashdim

import android.os.VibrationEffect

internal object Vibrator {
    internal fun vibrate(vibrator: android.os.Vibrator, duration: Long) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(duration, 80)
        )
    }

    internal fun vibrateClick(vibrator: android.os.Vibrator) {
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    }

    internal fun vibrateDoubleClick(vibrator: android.os.Vibrator) {
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
    }

    internal fun vibrateTick(vibrator: android.os.Vibrator) {
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
    }
}
