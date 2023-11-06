package com.tyj.spotifycloneandroidapp.domain.exoplayer.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.tyj.spotifycloneandroidapp.R
import com.tyj.spotifycloneandroidapp.common.Constants
import com.tyj.spotifycloneandroidapp.domain.exoplayer.callbacks.SpotifyMusicPlayerNotificationListener
import com.tyj.spotifycloneandroidapp.domain.exoplayer.notification.SpotifyMusicNotificationAdapter
import com.tyj.spotifycloneandroidapp.domain.exoplayer.notification.SpotifyMusicNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class SpotifyMusicService: MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var notificationManager: SpotifyMusicNotificationManager

//    var isForegroundService = false



    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("myDebug", "onStartCommand")
        if(intent?.action == "STOP_SERVICE") {

            stopForeground(Service.STOP_FOREGROUND_REMOVE)
            //isForegroundService = false
            stopSelf()
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.i("myDebug", "mediaItemCount: ${mediaSession.player.mediaItemCount}")
                notificationManager.startNotificationService(
                    mediaSession = mediaSession,
                    mediaSessionService = this
                )
//            isForegroundService = true
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i("myDebug", "onDestroy in SpotifyMusicService")
        mediaSession.apply {
            release()
            if(player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
                player.release()
            }
        }

    }

}