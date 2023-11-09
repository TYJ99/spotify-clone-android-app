package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
fun SongPlayerOnPlayingSongScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onRewind: () -> Unit,
    onFastForward: () -> Unit,
    modifier: Modifier = Modifier,
    songDuration: Long,
) {

    Box (
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Center
            ) {
                val paddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                Slider(
                    value = progress,
                    onValueChange = { onProgress(it) },
                    valueRange = 0f..100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                )

                //Spacer(modifier = Modifier.size(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                ) {
                    // (currentProgress.toFloat() / duration.toFloat()) * 100f
                    val currentTime = ((progress / 100f) * songDuration).toLong()
                    Text(
                        text = timeStampToDuration(currentTime),
                        textAlign = TextAlign.Start,
                    )

                    Text(
                        text = timeStampToDuration(songDuration),
                        textAlign = TextAlign.End,
                    )

                }
            }


            Spacer(modifier = Modifier.size(8.dp))

            MediaPlayerControllerOnPlayingSongScreen(
                isAudioPlaying = isAudioPlaying,
                onStart = onStart,
                onNext = onNext,
                onPrevious = onPrevious,
                onRewind = onRewind,
                onFastForward = onFastForward,
                modifier = Modifier
//                    .fillMaxWidth()
                    .padding(4.dp)
                    .weight(3f)
            )
        }

    }
}

private fun timeStampToDuration(position: Long): String {
    val totalSecond = floor(position / 1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
}