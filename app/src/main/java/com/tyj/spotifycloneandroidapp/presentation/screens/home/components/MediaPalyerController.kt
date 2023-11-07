package com.tyj.spotifycloneandroidapp.presentation.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyj.spotifycloneandroidapp.presentation.ui.theme.SpotifyCloneAndroidAppTheme

@Composable
fun MediaPlayerController(
    modifier: Modifier,
    isAudioPlaying: Boolean,
    traditionalPlayerToggle: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        /*
        modifier = Modifier
            .height(56.dp)
            .padding(4.dp)

         */
        modifier = modifier
    ) {
        val iconSize = Modifier
            .weight(1f)

        if(traditionalPlayerToggle) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                modifier = iconSize
                    .clickable {
                    onPrevious()
                },
                contentDescription = "SkipPrevious button on the Bottom Bar Media player controller"
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        PlayerIconItem(
            icon = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow,
            modifier = if(!traditionalPlayerToggle) modifier.size(50.dp) else iconSize
        ) {
            onStart()
        }
        Spacer(modifier = Modifier.size(8.dp))
        if(traditionalPlayerToggle) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                modifier = iconSize
                    .clickable {
                    onNext()
                },
                contentDescription = "SkipNext button on the Bottom Bar Media player controller"
            )
            Spacer(modifier = Modifier.size(4.dp))
        }

    }
}

@Preview
@Composable
fun MediaPlayerControlPrev() {
    SpotifyCloneAndroidAppTheme {
        MediaPlayerController(
            modifier = Modifier,
            isAudioPlaying = false,
            traditionalPlayerToggle = true,
            onStart = {},
            onNext = {},
            onPrevious = {},
        )
    }
}