package com.tyj.spotifycloneandroidapp.presentation.screens.home.components

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.common.MusicBarAnimation
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.floor

@Composable
fun AudioItem(
    song: Song,
    onItemClick: () -> Unit,
    onLoadSongImage: (Context, Song) -> StateFlow<Bitmap?>,
    currPlayingSong: Song,
    isAudioPlaying: Boolean,
) {

    val songImage by onLoadSongImage(LocalContext.current, song).collectAsStateWithLifecycle()
    var cardHeight by remember {
        mutableStateOf(0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                onItemClick()
            }.onSizeChanged {
                Log.i("myDebugMusicBarAnimation", "Card size changed, ${it.height}")
                cardHeight = it.height
            },
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            songImage?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "${song.title} image",
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(3f)
//                    contentScale = ContentScale.Fit

                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Column(
                modifier = Modifier
                    .weight(9f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = song.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }
            Spacer(modifier = Modifier.size(8.dp))
        }

        // add MusicBarAnimation
        if(song == currPlayingSong) {
            Log.i("myDebugMusicBarAnimation", "song: $song")
            Log.i("myDebugMusicBarAnimation", "currPlayingSong: $currPlayingSong")
            MusicBarAnimation(
                isAudioPlaying = isAudioPlaying,
                height = cardHeight,
            )
        }

        /*
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxSize(),
            //contentAlignment = Alignment.Center
        ) {




        }

         */


    }
}

private fun timeStampToDuration(position: Long): String {
    val totalSecond = floor(position / 1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
}