package com.tyj.spotifycloneandroidapp.presentation.common.components

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun MusicBarAnimation(
    isAudioPlaying: Boolean,
    height: Int,
    color: Color = MaterialTheme.colorScheme.surface,
) {
    Log.i("myDebugMusicBarAnimation", "In MusicBarAnimation, (Re)Compose")
    Log.i("myDebugMusicBarAnimation", "height: $height")


    val musicBarAnimation = rememberInfiniteTransition(label = "musicBarAnimation")

    val musicBarHeight1 by musicBarAnimation.animateFloat(
        initialValue = 0f,
        targetValue = if(isAudioPlaying) (height/4..height).random().toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "musicBarHeight1"
    )

    val musicBarHeight2 by musicBarAnimation.animateFloat(
        initialValue = 0f,
        targetValue = if(isAudioPlaying) (height/8..height).random().toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "musicBarHeight2"
    )

    val musicBarHeight3 by musicBarAnimation.animateFloat(
        initialValue = 0f,
        targetValue = if(isAudioPlaying) (height/8..height).random().toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            tween(300),
            repeatMode = RepeatMode.Reverse
        ),
        label = "musicBarHeight3"
    )

    val musicBarHeight4 by musicBarAnimation.animateFloat(
        initialValue = 0f,
        targetValue = if(isAudioPlaying) (height/8..height).random().toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "musicBarHeight4"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .rotate(180F) // rotate so rectangles get drawn from the bottom
            .background(color.copy(0.4f))
    ) {
        val canvasWidth = this.size.width
        Log.i("myDebugMusicBarAnimation", "canvasWidth: $canvasWidth")


        /* draw 8 rectangles along the canvas with a transparent color and a random height
         an Offset, is configured to provide equal spacing between the bars
         animation begins once turntable arm rotation is complete and will continue if music is playing
         */

        for (i in 0..7) {
            when(i % 4) {
                0 -> {
                    drawRect(
                        color = color.copy(0.4f),
                        size = Size(canvasWidth / 9, musicBarHeight1),
                        topLeft = Offset(x = canvasWidth / 8 * i.toFloat(), y = 0f)
                    )
                }
                1 -> {
                    drawRect(
                        color = color.copy(0.4f),
                        size = Size(canvasWidth / 9, musicBarHeight2),
                        topLeft = Offset(x = canvasWidth / 8 * i.toFloat(), y = 0f)
                    )
                }
                2 -> {
                    drawRect(
                        color = color.copy(0.4f),
                        size = Size(canvasWidth / 9, musicBarHeight3),
                        topLeft = Offset(x = canvasWidth / 8 * i.toFloat(), y = 0f)
                    )
                }
                3 -> {
                    drawRect(
                        color = color.copy(0.4f),
                        size = Size(canvasWidth / 9, musicBarHeight4),
                        topLeft = Offset(x = canvasWidth / 8 * i.toFloat(), y = 0f)
                    )
                }
            }
        }
    }

}