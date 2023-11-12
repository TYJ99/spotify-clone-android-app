package com.tyj.spotifycloneandroidapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.tyj.spotifycloneandroidapp.common.Constants.ALBUM_COLLECTION
import com.tyj.spotifycloneandroidapp.common.Constants.SONG_COLLECTION
import com.tyj.spotifycloneandroidapp.domain.model.Album
import com.tyj.spotifycloneandroidapp.domain.model.Song
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpotifyMusicDatabase @Inject constructor() : MusicDatabase {
    private val firebase = FirebaseFirestore.getInstance()
    private val songCollection = firebase.collection(SONG_COLLECTION)
    private val albumCollection = firebase.collection(ALBUM_COLLECTION)

    override suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAllAlbums(): List<Album> {
        return try {
            albumCollection.get().await().toObjects(Album::class.java)
        } catch (e: Exception) {
            emptyList()
        }

        /*
        albumCollection
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val song = document.toObject(Song::class.java)
                    val albumArtist = song.subtitle
                    val songs = artistSongMap.getOrDefault(albumArtist, mutableListOf())
                    songs.add(song)
                    artistSongMap[albumArtist] = songs

                    Log.d("myDebugAddDataToFirebase", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("myDebugAddDataToFirebase", "Error getting documents.", exception)
            }

         */
    }
}