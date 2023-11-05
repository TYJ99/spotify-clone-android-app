package com.tyj.spotifycloneandroidapp.di

import androidx.media3.session.MediaSession
import com.tyj.spotifycloneandroidapp.data.remote.MusicDatabase
import com.tyj.spotifycloneandroidapp.data.repository.FirebaseMusicRepository
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideServiceHandler(
        mediaSession: MediaSession,
    ): SpotifyMusicServiceHandler =
        SpotifyMusicServiceHandler(mediaSession)

    @Provides
    @ViewModelScoped
    fun provideMusicRepository(@BindFakeMusicDatabase musicDatabase: MusicDatabase): MusicRepository {
        return FirebaseMusicRepository(musicDatabase)
    }

}