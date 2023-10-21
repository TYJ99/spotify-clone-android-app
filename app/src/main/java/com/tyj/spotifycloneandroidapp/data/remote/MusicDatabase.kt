package com.tyj.spotifycloneandroidapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.tyj.spotifycloneandroidapp.common.Constants.SONG_COLLECTION
import com.tyj.spotifycloneandroidapp.data.entities.Song
import kotlinx.coroutines.tasks.await
/*
    Todo: Try to use local database for caching songs in local package
    Todo: Try to use pagination for loading data from firestore more efficiently

 */
class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

}