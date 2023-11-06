package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SongImageOnPlayingSongScreen(
    onLoadSongImage: (Context) -> StateFlow<Bitmap?>,
    modifier: Modifier = Modifier
) {
    val songImage by onLoadSongImage(LocalContext.current).collectAsStateWithLifecycle()

    songImage?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "song image on the playing song screen",
            modifier = modifier,
        )
    }
}