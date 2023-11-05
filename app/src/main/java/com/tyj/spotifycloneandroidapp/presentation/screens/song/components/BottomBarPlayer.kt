package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.ui.theme.SpotifyCloneAndroidAppTheme

@Composable
fun BottomBarPlayer(
    progress: Float,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    currPlayingSong: Song,
    songList: List<Song>,
    traditionalPlayerToggle: Boolean,
    onPagerSwipe: (Int) -> Unit,
) {

    BottomAppBar(
        content = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpotifyArtistInfo(
                        modifier = if(traditionalPlayerToggle) Modifier.weight(7f) else Modifier.weight(8f),
                        currPlayingSong = currPlayingSong,
                        songList = songList,
                        traditionalPlayerToggle = traditionalPlayerToggle,
                        onPagerSwipe = onPagerSwipe,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    MediaPlayerController(
                        isAudioPlaying = isAudioPlaying,
                        traditionalPlayerToggle = traditionalPlayerToggle,
                        onStart = onStart,
                        onNext = onNext,
                        onPrevious = onPrevious,
                        modifier = if(traditionalPlayerToggle) Modifier.weight(3f) else Modifier.weight(2f),
                    )
                    /*
                    if(traditionalPlayerToggle) {
                        Slider(
                            value = progress,
                            onValueChange = { onProgress(it) },
                            valueRange = 0f..100f,
                            modifier = Modifier.weight(5f)
                        )
                    }

                     */
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BottomBarPlayerPrev() {
    SpotifyCloneAndroidAppTheme {
        BottomBarPlayer(
            progress =  5f,
            onProgress = { },
            isAudioPlaying = true,
            onStart = {},
            onNext = {},
            onPrevious = {},
            currPlayingSong = Song("1", "title1", "subtitle1", "", ""),
            songList = listOf(
                Song("1", "title1", "subtitle1", "", ""),
                Song("2", "title2", "subtitle2", "", ""),
                Song("3", "title3", "subtitle3", "", ""),
                Song("4", "title4", "subtitle4", "", "")

            ),
            traditionalPlayerToggle = true,
        ) {}

    }

}