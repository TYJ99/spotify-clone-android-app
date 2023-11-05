package com.tyj.spotifycloneandroidapp.data.repository

import com.tyj.spotifycloneandroidapp.data.remote.MusicDatabase
import com.tyj.spotifycloneandroidapp.di.BindFakeMusicDatabase
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicRepository @Inject constructor(
    @BindFakeMusicDatabase private val musicDatabase: MusicDatabase
): MusicRepository {
    override suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        musicDatabase.getAllSongs()
    }
}