package com.tyj.spotifycloneandroidapp.di

import com.tyj.spotifycloneandroidapp.data.remote.FakeMusicDatabase
import com.tyj.spotifycloneandroidapp.data.remote.MusicDatabase
import com.tyj.spotifycloneandroidapp.data.remote.SpotifyMusicDatabase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BindSpotifyMusicDatabase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BindFakeMusicDatabase

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicDatabaseModule {
    @BindSpotifyMusicDatabase
    @Binds
    @ViewModelScoped
    abstract fun bindSpotifyMusicDatabase(musicDatabase: SpotifyMusicDatabase): MusicDatabase

    @BindFakeMusicDatabase
    @Binds
    @ViewModelScoped
    abstract fun bindFakeMusicDatabase(musicDatabase: FakeMusicDatabase): MusicDatabase
}