package com.tyj.spotifycloneandroidapp.di

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import androidx.media3.common.C
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.AudioAttributes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.tyj.spotifycloneandroidapp.data.remote.MusicDatabase
import com.tyj.spotifycloneandroidapp.data.repository.FirebaseMusicRepository
import com.tyj.spotifycloneandroidapp.domain.exoplayer.notification.SpotifyMusicNotificationManager
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideMusicDatabase() = MusicDatabase()

    @ServiceScoped
    @Provides
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @ServiceScoped
    @Provides
    @UnstableApi
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ) = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @ServiceScoped
    @Provides
    fun provideActivityPendingIntent(application: Application,
                              @ApplicationContext context: Context): PendingIntent {
        val packageName = application.packageName
        val activityPendingIntent = application.packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        return activityPendingIntent!!
    }

    @ServiceScoped
    @Provides
    fun provideMediaSession(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer,
        activityPendingIntent: PendingIntent
    ): MediaSession = MediaSession.Builder(context, exoPlayer)
        .setSessionActivity(activityPendingIntent)
        .build()

    @Provides
    @ServiceScoped
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer,
    ): SpotifyMusicNotificationManager = SpotifyMusicNotificationManager(
        context = context,
        exoPlayer = exoPlayer
    )

    @Provides
    @ServiceScoped
    fun provideServiceHandler(exoPlayer: ExoPlayer): SpotifyMusicServiceHandler =
        SpotifyMusicServiceHandler(exoPlayer)

    @ServiceScoped
    @Provides
    @Singleton
    fun provideMusicRepository(musicDatabase: MusicDatabase): MusicRepository {
        return FirebaseMusicRepository(musicDatabase)
    }

    @ServiceScoped
    @Provides
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ) = DefaultDataSource.Factory(context)

}