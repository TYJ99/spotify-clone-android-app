package com.tyj.spotifycloneandroidapp.di

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.tyj.spotifycloneandroidapp.GlideApp
import com.tyj.spotifycloneandroidapp.R
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = GlideApp.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)

    )

    @Provides
    @Singleton
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @UnstableApi
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ) = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .setSeekForwardIncrementMs(5000L)
        .setSeekBackIncrementMs(5000L)
        //.setPauseAtEndOfMediaItems(true)
        .build()

    @Singleton
    @Provides
    fun provideActivityPendingIntent(application: Application,
                                     @ApplicationContext context: Context): PendingIntent {
        val packageName = application.packageName
        val activityPendingIntent = application.packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        return activityPendingIntent!!
    }

    @Singleton
    @Provides
    fun provideMediaSession(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer,
        activityPendingIntent: PendingIntent
    ): MediaSession = MediaSession.Builder(context, exoPlayer)
        .setSessionActivity(activityPendingIntent)
        .build()

    @Provides
    @Singleton
    fun provideSpotifyMusicService() = SpotifyMusicService()


}