package com.tyj.spotifycloneandroidapp.domain.repository

import com.tyj.spotifycloneandroidapp.domain.model.Song

interface MusicRepository {

    suspend fun fetchMediaData(): List<Song>

}