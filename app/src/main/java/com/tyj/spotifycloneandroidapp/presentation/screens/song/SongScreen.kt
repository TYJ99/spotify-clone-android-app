package com.tyj.spotifycloneandroidapp.presentation.screens.song

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.AudioItem
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.BottomBarPlayer
import com.tyj.spotifycloneandroidapp.presentation.ui.theme.SpotifyCloneAndroidAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean,
    currentPlayingAudio: Song,
    songList: List<Song>,
    onStart: () -> Unit,
    onItemClick: (Int) -> Unit,
    onNext: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomBarPlayer(
                progress = progress,
                onProgress = onProgress,
                song = currentPlayingAudio,
                onStart = onStart,
                onNext = onNext,
                isAudioPlaying = isAudioPlaying
            )
        }
    ) {
        LazyColumn(
            contentPadding = it
        ) {
            itemsIndexed(songList) { index, audio ->
                AudioItem(
                    song = audio,
                    onItemClick = { onItemClick(index) }
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun SongScreenPrev() {
    SpotifyCloneAndroidAppTheme {
        SongScreen(
            progress = 50f,
            onProgress = {},
            isAudioPlaying = true,
            songList = listOf(
                Song("1", "Title One", "Artist 1", "", ""),
                Song("2", "Title Two", "Artist 2", "", ""),
            ),
            currentPlayingAudio = Song("", "Title One", "Artist 1", "", ""),
            onStart = {},
            onItemClick = {},
            onNext = {}
        )
    }
}