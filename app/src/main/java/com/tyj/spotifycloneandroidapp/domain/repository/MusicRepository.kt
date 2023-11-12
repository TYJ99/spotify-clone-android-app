package com.tyj.spotifycloneandroidapp.domain.repository

import com.tyj.spotifycloneandroidapp.domain.model.Album
import com.tyj.spotifycloneandroidapp.domain.model.Song

interface MusicRepository {

    suspend fun fetchMediaData(): List<Song>
    suspend fun fetchAlbumsData(): List<Album>

}