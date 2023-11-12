package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.tyj.spotifycloneandroidapp.domain.model.Song

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongInfo(
    song: Song,
    modifier: Modifier = Modifier
) {
    var songInfo = "No Song is Playing"
    if(song.mediaId != "") songInfo = "${song.title} - ${song.subtitle}"
    Text(
        text = songInfo,
        modifier = modifier.basicMarquee(),
        fontSize = 24.sp,
    )

}