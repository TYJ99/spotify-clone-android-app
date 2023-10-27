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
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicService
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer,
        spotifyMusicService: SpotifyMusicService
    ): SpotifyMusicNotificationManager = SpotifyMusicNotificationManager(
        context = context,
        exoPlayer = exoPlayer,
        spotifyMusicService = spotifyMusicService
    )


    @ServiceScoped
    @Provides
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ) = DefaultDataSource.Factory(context)

}