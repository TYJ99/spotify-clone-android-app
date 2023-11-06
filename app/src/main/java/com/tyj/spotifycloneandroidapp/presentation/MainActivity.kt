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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.session.MediaSession
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.PlayerEvent
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicService
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
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

    @Inject
    lateinit var musicServiceHandler: SpotifyMusicServiceHandler

    @Inject
    lateinit var spotifyMusicService: SpotifyMusicService

    @Inject
    lateinit var mediaSession: MediaSession

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                val songListState = viewModel.songList.collectAsStateWithLifecycle()
                val currentSelectedSong by viewModel.currentSelectedSong.collectAsStateWithLifecycle()
                val toggleState by viewModel.traditionalPlayerToggle.collectAsStateWithLifecycle()
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

                    /*
                    HomeScreen(
                        progress = viewModel.progress,
                        onProgress = { viewModel.onUiEvents(UIEvents.SeekTo(it)) },
                        currPlayingSong = currentSelectedSong,
                        isAudioPlaying = viewModel.isPlaying,
                        songListState = songListState,
                        onStart = {
                            viewModel.onUiEvents(UIEvents.PlayPause)
                        },
                        onItemClickOrSwipe = { index, isClickSong, traditionalPlayerToggle ->
                            viewModel.onUiEvents(
                                uiEvents =  UIEvents.SelectedSongChange(index),
                                isClickSong = isClickSong,
                                traditionalPlayerToggle = traditionalPlayerToggle,
                            )
                            startService()
                        },
                        onNext = {
                            viewModel.onUiEvents(UIEvents.SeekToNext)
                        },
                        onPrevious = {
                            viewModel.onUiEvents(UIEvents.SeekToPrevious)
                        },
                        onLoadSongImage = { context, song ->
                            viewModel.loadSongImage(context, song)
                        },
                        toggleState = toggleState,
                        onToggle = { toggleState ->
                            viewModel.onTraditionalPlayerToggle(toggleState)
                        },
                        navController = navController,
                    )

                     */


                }

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
        isServiceRunning = false
        CoroutineScope(Dispatchers.Main).launch {
            val job = launch {
                musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
                stopSpotifyMusicService()
            }
            job.join()
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