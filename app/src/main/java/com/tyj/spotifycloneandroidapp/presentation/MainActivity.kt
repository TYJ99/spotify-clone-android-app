package com.tyj.spotifycloneandroidapp.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.session.MediaSession
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.firestore.FirebaseFirestore
import com.tyj.spotifycloneandroidapp.common.Constants
import com.tyj.spotifycloneandroidapp.common.Constants.ALBUM_IMAGE_1
import com.tyj.spotifycloneandroidapp.common.Constants.ALBUM_IMAGE_2
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.PlayerEvent
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicService
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.navigation.SetupNavGraph
import com.tyj.spotifycloneandroidapp.presentation.screens.home.HomeViewModel
import com.tyj.spotifycloneandroidapp.presentation.ui.theme.SpotifyCloneAndroidAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private var isServiceRunning = false
    private var artistSongMap: MutableMap<String, MutableList<Song>> = mutableMapOf()
    private val firebase = FirebaseFirestore.getInstance()


    @Inject
    lateinit var musicServiceHandler: SpotifyMusicServiceHandler

    @Inject
    lateinit var spotifyMusicService: SpotifyMusicService

    @Inject
    lateinit var mediaSession: MediaSession


    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        /*
        // add data to database
        CoroutineScope(Dispatchers.IO).launch {
            retrieveDataFromFirebase(artistSongMap)
            delay(5000L)
            addDataToFirebase(artistSongMap)
        }

         */

        Log.i("myDebug", "initialize viewModel")
        setContent {
            SpotifyCloneAndroidAppTheme {
                // A surface container using the 'background' color from the theme
                // If you want to insert songs from external storage, ask for permission.
                val permissionState = rememberPermissionState(
                    permission = Manifest.permission.READ_EXTERNAL_STORAGE
                )
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            permissionState.launchPermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

//                val songListState = viewModel.songList.collectAsStateWithLifecycle()
//                val currentSelectedSong by viewModel.currentSelectedSong.collectAsStateWithLifecycle()
//                val toggleState by viewModel.traditionalPlayerToggle.collectAsStateWithLifecycle()
                val navController = rememberNavController()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SetupNavGraph(
                        navController = navController,
                        startService = {
                            startService()
                        },
                    )
                }
            }
        }
    }

    private fun retrieveDataFromFirebase(
        artistSongMap: MutableMap<String, MutableList<Song>>
    ) {
        Log.d("myDebugAddDataToFirebase", "retrieveDataFromFirebase")

        val songCollection = firebase.collection(Constants.SONG_COLLECTION)

        songCollection
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

    }

    private fun addDataToFirebase(
        artistSongMap: MutableMap<String, MutableList<Song>>
    ) {
        // Create a new user with a first and last name
        // data class Album(
        //    val title: String = "",
        //    val artist: String = "",
        //    val imageUrl: String = "",
        //    val songs: List<Song> = listOf<Song>(),
        //)

        Log.d("myDebugAddDataToFirebase", "artistSongMap: $artistSongMap")


        val albumImageUrls = listOf<String>(
            ALBUM_IMAGE_1,
            ALBUM_IMAGE_2
        )

        val albumTitles = mutableListOf<String>()
        for(i in 0 .. 50) {
            albumTitles.add("album$i")
        }

        for((index, songs) in artistSongMap.values.withIndex()) {
            val song = songs[0]
            val album = hashMapOf(
                "title" to "${albumTitles[index]} - ${song.subtitle}",
                "artist" to song.subtitle,
                "imageUrl" to albumImageUrls.random(),
                "songs" to songs
            )

            // Add a new document with a generated ID
            firebase.collection("albums")
                .add(album)
                .addOnSuccessListener { documentReference ->
                    Log.d("myDebugAddDataToFirebase", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("myDebugAddDataToFirebase", "Error adding document", e)
                }
        }
    }

    private fun startService() {
        Log.i("myDebug", "startService, isServiceRunning is $isServiceRunning")
        if (!isServiceRunning) {
            val intent = Intent(this, SpotifyMusicService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isServiceRunning = true
        }
    }

    private fun stopSpotifyMusicService() {
        Log.i("myDebug", "stopSpotifyMusicService")
        val intent = Intent(this, SpotifyMusicService::class.java)
        intent.action = "STOP_SERVICE"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i("myDebug", "MainActivity onDestroy")
        if(isServiceRunning) {
            isServiceRunning = false
            CoroutineScope(Dispatchers.Main).launch {
                val job = launch {
                    musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
                    stopSpotifyMusicService()
                }
                job.join()
                exitProcess(0)
            }
        } else {
            exitProcess(0)
        }


//        GlobalScope.launch(Dispatchers.Main) {
//            musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
//        }
//        exitProcess(0)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotifyCloneAndroidAppTheme {
        Greeting("Android")
    }
}