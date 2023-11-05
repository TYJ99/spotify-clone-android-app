package com.tyj.spotifycloneandroidapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.tyj.spotifycloneandroidapp.common.Constants
import com.tyj.spotifycloneandroidapp.domain.model.Song
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpotifyMusicDatabase @Inject constructor() : MusicDatabase {
    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(Constants.SONG_COLLECTION)

    override suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}