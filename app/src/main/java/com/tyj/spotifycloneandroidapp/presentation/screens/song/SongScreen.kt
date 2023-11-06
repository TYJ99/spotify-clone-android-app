package com.tyj.spotifycloneandroidapp.presentation.screens.song

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyj.spotifycloneandroidapp.common.BackInvokeHandler
import com.tyj.spotifycloneandroidapp.common.BackPressHandler
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.SongImageOnPlayingSongScreen
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.SongInfo
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.SongPlayerOnPlayingSongScreen
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.SongScreenTopBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SongScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    currPlayingSong: Song,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onLoadSongImage: (Context, Song) -> StateFlow<Bitmap?>,
    onFastForward: () -> Unit,
    onRewind: () -> Unit,
    navController: NavHostController,
    songDuration: Long,
) {

    val onBackPressed = { navController.popBackStack() }

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        BackInvokeHandler(
            handleBackHandler = true,
            onBackPressed = {
                onBackPressed()
            }
        )
    } else {
        BackHandler(enabled = true) {
            Log.i("myDebugPressBackButton", "On Song Screen")
            Log.i("myDebugPressBackButton", "handle back pressed")
            onBackPressed()
        }
    }



//    BackPressHandler(
//        onBackPressed = { onBackPressed() },
//    )

    Scaffold(
        topBar = {
            SongScreenTopBar(
                onBackPressed = {
                    onBackPressed()
                }
            )
        }
    ) {
        val paddingValues = PaddingValues(horizontal = 8.dp, vertical = 24.dp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // song info
            SongInfo(
                song = currPlayingSong,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.size(4.dp))

            // song image, TODO: animation
            SongImageOnPlayingSongScreen(
                onLoadSongImage = { context ->
                    onLoadSongImage(context, currPlayingSong)
                },
                modifier = Modifier.weight(5f),
            )

            Spacer(modifier = Modifier.size(4.dp))

            // song player
            SongPlayerOnPlayingSongScreen(
                progress = progress,
                onProgress = onProgress,
                isAudioPlaying = isAudioPlaying,
                onStart = onStart,
                onNext = onNext,
                onPrevious = onPrevious,
                onRewind = onRewind,
                onFastForward = onFastForward,
                modifier = Modifier.weight(4f),
                songDuration = songDuration,
            )


            /*
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(horizontal = 8.dp, vertical = 24.dp)),
                contentAlignment = Alignment.Center,
            ){

            }

             */

        }


    }
}

@Preview
@Composable
fun SongScreenPrev(){
    SongScreen(
        progress = 0f,
        onProgress = {},
        currPlayingSong = Song("", "title1", "subtitle1", "", ""),
        isAudioPlaying = true,
        onStart = {},
        onNext = {},
        onPrevious = {},
        onLoadSongImage = { _, _ ->
            MutableStateFlow(null)
        },
        onFastForward = {},
        onRewind = {},
        navController = rememberNavController(),
        songDuration = 186000L,
    )
}