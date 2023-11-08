package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SongImageOnPlayingSongScreen(
    onLoadSongImage: (Context) -> StateFlow<Bitmap?>,
    isAudioPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val songImage by onLoadSongImage(LocalContext.current).collectAsStateWithLifecycle()

    val transition = updateTransition(
        targetState = isAudioPlaying,
        label = null
    )

    val imageSize by transition.animateInt(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "borderRadius",
        targetValueByState = { it ->

            if(it) 3 else 2
        },

    )

    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val imageSizeInfinite by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue =  2f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "imageSize",

    )

    val imageRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue =  1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Restart,
        ),
        label = "imageSize"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        songImage?.let {
            val currImageSize = if(isAudioPlaying) imageSizeInfinite else 3f
            //Log.i("myDebugAnimation", "currImageSize: $currImageSize")
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "song image on the playing song screen",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size((currImageSize * 100).dp)
                    .clip(CircleShape)
                    .graphicsLayer {
                        rotationZ = if(isAudioPlaying) 360f * imageRotation else 0f
                    }
            )
        }
    }


}