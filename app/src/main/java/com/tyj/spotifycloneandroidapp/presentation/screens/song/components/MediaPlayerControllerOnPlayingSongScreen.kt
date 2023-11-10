package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.tyj.spotifycloneandroidapp.presentation.common.MusicBarAnimation
import com.tyj.spotifycloneandroidapp.presentation.screens.home.components.PlayerIconItem

@Composable
fun MediaPlayerControllerOnPlayingSongScreen(
    modifier: Modifier,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onRewind: () -> Unit,
    onFastForward: () -> Unit,
    //height: Int,
) {

    var rowHeight by remember {
        mutableStateOf(0)
    }

    Box(
        modifier
            .fillMaxSize()
            .onSizeChanged {
                rowHeight = it.height
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val iconSize = Modifier.size(50.dp)
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                modifier = iconSize.clickable {
                    onPrevious()
                },
                contentDescription = "SkipPrevious button on the playing song screen"
            )

            Spacer(modifier = Modifier.size(8.dp))

            Icon(
                imageVector = Icons.Default.FastRewind,
                modifier = iconSize.clickable {
                    onRewind()
                },
                contentDescription = "Rewind button on the playing song screen"
            )

            Spacer(modifier = Modifier.size(8.dp))

            PlayerIconItem(
                icon = if (isAudioPlaying) Icons.Default.Pause
                else Icons.Default.PlayArrow,
                modifier = iconSize
            ) {
                onStart()
            }

            Spacer(modifier = Modifier.size(8.dp))

            Icon(
                imageVector = Icons.Default.FastForward,
                modifier = iconSize.clickable {
                    onFastForward()
                },
                contentDescription = "Fast forward button on the playing song screen"
            )
            Spacer(modifier = Modifier.size(8.dp))

            Icon(
                imageVector = Icons.Default.SkipNext,
                modifier = iconSize.clickable {
                    onNext()
                },
                contentDescription = "SkipNext button on the playing song screen"
            )
        }

        MusicBarAnimation(
            isAudioPlaying = isAudioPlaying,
            height = rowHeight,
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}