package com.tyj.spotifycloneandroidapp.domain.exoplayer.callbacks

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.tyj.spotifycloneandroidapp.common.Constants
import com.tyj.spotifycloneandroidapp.common.Constants.NOTIFICATION_ID
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.MusicService
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicService

@UnstableApi
class SpotifyMusicPlayerNotificationListener (
    private val musicService: SpotifyMusicService,
    private val context: Context
) : PlayerNotificationManager.NotificationListener {

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicService.apply {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
//            isForegroundService = false
            stopSelf()
            Log.i("myDebug", "onNotificationCancelled")
        }
    }

    /*
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if(ongoing && !isForegroundService) {
                val intent = Intent(context, SpotifyMusicService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
                startForeground(NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }

    }

     */
}