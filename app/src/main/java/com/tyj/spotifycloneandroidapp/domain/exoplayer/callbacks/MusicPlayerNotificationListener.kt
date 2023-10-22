package com.tyj.spotifycloneandroidapp.domain.exoplayer.callbacks

import android.app.Notification
import android.app.Service.STOP_FOREGROUND_DETACH
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.tyj.spotifycloneandroidapp.common.Constants.NOTIFICATION_ID
import com.tyj.spotifycloneandroidapp.domain.exoplayer.MusicService

@UnstableApi
class MusicPlayerNotificationListener (
    private val musicService: MusicService
) : PlayerNotificationManager.NotificationListener {

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicService.apply {
            stopForeground(STOP_FOREGROUND_DETACH)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if(ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    this,
                    Intent(applicationContext, this::class.java) // "this" refer to musicService
                )
                startForeground(NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }
    }
}
