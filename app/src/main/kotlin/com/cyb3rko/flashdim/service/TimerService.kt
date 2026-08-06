/*
 * Copyright (c) 2022-2025 Cyb3rKo
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

package com.cyb3rko.flashdim.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.cyb3rko.flashdim.Camera
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.activities.MainActivity

/**
 * Keeps the flashlight timer alive, even if the app itself gets closed in the meantime.
 *
 * The responsibilities are split on purpose:
 * - the activity switches the light *on*, because it owns the light level and the matching UI,
 * - this service counts down and switches the light *off* again.
 *
 * All stop paths (timer expired, "Stop" in the dialog, "Stop" in the notification) end up in
 * [finish], so the UI is always informed through [onFinished].
 */
internal class TimerService : Service() {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val finishRunnable = Runnable { finish() }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start(intent.getIntExtra(EXTRA_DURATION_MINUTES, 1))
            ACTION_STOP -> finish()
        }
        // The timer always is the result of an explicit user action, so there is nothing
        // meaningful to restore if the system kills the service
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(finishRunnable)
        isRunning = false
        super.onDestroy()
    }

    private fun start(durationMinutes: Int) {
        mainHandler.removeCallbacks(finishRunnable)
        val durationMillis = durationMinutes * 60 * 1000L
        startForeground(NOTIFICATION_ID, buildNotification(durationMillis))
        isRunning = true
        // The notification counts down on its own, so a single delayed callback is enough.
        // It doesn't run while the device is in deep sleep, which means the light can stay on
        // slightly longer than requested - but never shorter.
        mainHandler.postDelayed(finishRunnable, durationMillis)
    }

    private fun finish() {
        mainHandler.removeCallbacks(finishRunnable)
        isRunning = false
        Camera.turnOffFlashlight(this)
        onFinished?.invoke()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.dialog_timer_notification_channel),
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService<NotificationManager>()?.createNotificationChannel(channel)
    }

    private fun buildNotification(durationMillis: Long): Notification {
        val openApp = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val stopTimer = PendingIntent.getService(
            this,
            0,
            Intent(this, TimerService::class.java).setAction(ACTION_STOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.dialog_timer_notification_title))
            .setContentText(getString(R.string.dialog_timer_notification_text))
            .setSmallIcon(R.drawable.ic_timer)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            // Let the system render the remaining time, instead of updating every second
            .setWhen(System.currentTimeMillis() + durationMillis)
            .setUsesChronometer(true)
            .setChronometerCountDown(true)
            .setContentIntent(openApp)
            .addAction(0, getString(R.string.dialog_timer_button_stop), stopTimer)
            .build()
    }

    companion object {
        private const val ACTION_START = "com.cyb3rko.flashdim.TIMER_START"
        private const val ACTION_STOP = "com.cyb3rko.flashdim.TIMER_STOP"
        private const val EXTRA_DURATION_MINUTES = "duration_minutes"
        private const val CHANNEL_ID = "flashdim_timer_channel"
        private const val NOTIFICATION_ID = 1001

        // True while the countdown is running, used to restore the state of the timer dialog
        @Volatile
        var isRunning = false
            private set

        /**
         * Called on the main thread once the light has been switched off again, no matter which
         * of the stop paths was taken.
         *
         * Set by [MainActivity] and cleared in its `onDestroy` to not leak the activity.
         */
        var onFinished: (() -> Unit)? = null

        fun start(context: Context, durationMinutes: Int) {
            val intent = Intent(context, TimerService::class.java)
                .setAction(ACTION_START)
                .putExtra(EXTRA_DURATION_MINUTES, durationMinutes)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            if (!isRunning) return
            context.startService(
                Intent(context, TimerService::class.java).setAction(ACTION_STOP)
            )
        }
    }
}
