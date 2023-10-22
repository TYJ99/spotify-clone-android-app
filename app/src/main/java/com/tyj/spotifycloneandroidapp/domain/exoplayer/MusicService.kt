package com.tyj.spotifycloneandroidapp.domain.exoplayer

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.tyj.spotifycloneandroidapp.domain.exoplayer.callbacks.MusicPlayerNotificationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

// class MusicService: MediaBrowserServiceCompat()
@AndroidEntryPoint
class MusicService: MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    /*
        In the legacy MediaSessionCompat world, the MediaSessionConnector was responsible for
        syncing the state of the player with the state of the session and receiving commands from
        controllers that needed delegation to appropriate player methods.
        With AndroidX Media3, this is done by the MediaSession directly without requiring a connector.

        1. Remove all references and usage of MediaSessionConnector:
           If you used the automated script to migrate ExoPlayer classes and packages,
           then the script likely has left your code in an uncompilable state regarding
           theMediaSessionConnector that can't be resolved.
           Android Studio will show you the broken code when you try to build or start the app.

        2. In the build.gradle file where you maintain your dependencies,
        add an implementation dependency to the AndroidX Media3 session module and
        remove the legacy dependency:
                                      implementation "androidx.media3:media3-session:1.1.1"

        3. Replace the MediaSessionCompat with androidx.media3.session.MediaSession.

        4. At the code site where you have created the legacy MediaSessionCompat,
           use androidx.media3.session.MediaSession.Builder to build a MediaSession.
           Pass the player to construct the session builder.
     */
    private lateinit var mediaSession: MediaSession
    //private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        /*
            Create an Intent that when we click on the notification, we want to open our activity.
         */

        // packageManager! (exclamation mark at the end):  They're called platform types and
        //                                                 they mean that Kotlin doesn't know whether
        //                                                 that value can or cannot be null and
        //                                                 it's up to you to decide if it's nullable or not.
        // https://stackoverflow.com/questions/43826699/single-exclamation-mark-in-kotlin

        // packageManager?.getLaunchIntentForPackage(packageName): a normal intent that just lead to out activity
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        mediaSession = MediaSession.Builder(this, exoPlayer)
            .setSessionActivity(activityIntent!!)
            .build()

        sessionToken = mediaSession.sessionCompatToken

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionCompatToken,
            MusicPlayerNotificationListener(this)
        ) {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}