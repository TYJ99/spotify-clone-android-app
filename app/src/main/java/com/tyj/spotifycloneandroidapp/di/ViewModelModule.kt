package com.tyj.spotifycloneandroidapp.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.tyj.spotifycloneandroidapp.data.remote.MusicDatabase
import com.tyj.spotifycloneandroidapp.data.repository.FirebaseMusicRepository
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideMusicDatabase() = MusicDatabase()

    @Provides
    @ViewModelScoped
    fun provideServiceHandler(exoPlayer: ExoPlayer): SpotifyMusicServiceHandler =
        SpotifyMusicServiceHandler(exoPlayer)

    @Provides
    @ViewModelScoped
    fun provideMusicRepository(musicDatabase: MusicDatabase): MusicRepository {
        return FirebaseMusicRepository(musicDatabase)
    }

}