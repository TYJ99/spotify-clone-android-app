package com.tyj.spotifycloneandroidapp.data.remote

import com.tyj.spotifycloneandroidapp.domain.model.Song
/*
    Todo: Try to use local database for caching songs in local package
    Todo: Try to use pagination for loading data from firestore more efficiently

 */
interface MusicDatabase {
    suspend fun getAllSongs(): List<Song>
}