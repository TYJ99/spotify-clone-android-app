package com.tyj.spotifycloneandroidapp.data.repository

import android.support.v4.media.MediaMetadataCompat
import com.tyj.spotifycloneandroidapp.data.remote.MusicDatabase
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicRepository @Inject constructor(
    private val musicDatabase: MusicDatabase
): MusicRepository {
    override suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        musicDatabase.getAllSongs()
    }
}