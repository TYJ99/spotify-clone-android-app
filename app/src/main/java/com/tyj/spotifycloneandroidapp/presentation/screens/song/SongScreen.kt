package com.tyj.spotifycloneandroidapp.presentation.screens.song

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.AudioItem
import com.tyj.spotifycloneandroidapp.presentation.screens.song.components.BottomBarPlayer
import com.tyj.spotifycloneandroidapp.presentation.ui.theme.SpotifyCloneAndroidAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun SongScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean,
    currentPlayingAudio: Song,
    songListState: State<List<Song>>,
    onStart: () -> Unit,
    onItemClick: (Int) -> Unit,
    onNext: () -> Unit,
    onLoadSongImage: (Context, Song) -> StateFlow<Bitmap?>
) {
    val songList by songListState
    val lazyColumnState = rememberLazyListState()
    Log.i("myDebug", "in SongScreen, songList $songList")
    Log.i("myDebug", "in SongScreen, songListState ${songListState.value}")
    /*
    LaunchedEffect(key1 = songList) {
        Log.i("myDebug", "LaunchedEffect in SongScreen, songList Changed, songList ${songList.size}")
//        songItemListState = emptyList()
//        songItemMapState = emptyMap()
//        isSongListChanged = true
    }

     */

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
            contentPadding = it,
            state = lazyColumnState
        ) {
            Log.i("myDebug", "recompose lazyColumn")
            itemsIndexed(songList) { index, song ->
                AudioItem(
                    song = song,
                    onItemClick = {
                        onItemClick(index)
                    },
                    onLoadSongImage = onLoadSongImage
                )

            }
        }

        /*
        LazyColumn(
            contentPadding = it
        ) {
            Log.i("myDebug", "LazyColumn in SongScreen recomposed, isSongListChanged : $isSongListChanged, songList : $songList")

            if(isSongListChanged) {
                itemsIndexed(songList) { index, song ->
                    val songImage by onLoadSongImage(LocalContext.current, song).collectAsStateWithLifecycle()
                    songImage?.let {
                        ++songImageCount
                    }
                    Log.i("myDebug", "songImage = $songImage")
                    songItemListState = songItemListState.toMutableList().apply{
                        songImage?.let {
                            Log.i("myDebug", "index = $index")
                            if(index == this.size) {
                                Log.i("myDebug", "add songItem to songItemListState")
                                add(index, SongItem(song, songImage))
                            }else {
                                Log.i("myDebug", "set songItem to songItemListState")
                                set(index, SongItem(song, songImage))
                            }
                        }
                    }
//                    Log.i("myDebug", "in isSongListChanged = true, songItemListState.size ${songItemListState.size}, songList.size: ${songList.size}")
//                    Log.i("myDebug", "isSongListChanged = $isSongListChanged, songItemListState = $songItemListState")

                }

            } else {
                Log.i("myDebug", "in isSongListChanged = false, songItemList = $songItemListState")
                itemsIndexed(songItemListState){ index, songItem ->
                    AudioItem(
                        song = songItem.song,
                        onItemClick = {
                            onItemClick(index)
                        },
                        songImage = songItem.songImage
                    )
                }
            }

            if(songList.size == songItemListState.size &&
                songImageCount >= songList.size &&
                songImageCount <= songList.size * 2) {
                Log.i("myDebug", "songList.size == songItemListState.size")
                isSongListChanged = false
            }

        }

         */
        /*
        LazyColumn(
            contentPadding = it
        ) {
            Log.i("myDebug", "LazyColumn in SongScreen recomposed, songList : ${songList.size}: $songList")

            itemsIndexed(songList) { index, song ->
                val songImage by onLoadSongImage(LocalContext.current, song).collectAsStateWithLifecycle()
                Log.i("myDebug", "songImage = $songImage")
                val songItem = songItemMapState[index]
                if( songItem != null &&
                    songImage == songItem.currSongImage &&
                    songItem.prevSongImage == songItem.currSongImage) {

                    Log.i("myDebug", "AudioItem: prev/curr: ${songItem.prevSongImage}, ${songItem.currSongImage}")
                    AudioItem(
                        song = songItem.song,
                        onItemClick = {
                            onItemClick(index)
                        },
                        songImage = songItem.currSongImage
                    )
                }else {
                    songItemMapState = songItemMapState.toMutableMap().apply {
                        songImage?.let {
                            val newSongItem = if(songItem != null) SongItem(song, songItem.currSongImage, songImage) else SongItem(song, null, songImage)
                            put(index, newSongItem)
                            Log.i("myDebug", "no AudioItem: prev/curr: ${newSongItem.prevSongImage}, ${newSongItem.currSongImage}")
                        }
                    }
                }
//                    Log.i("myDebug", "in isSongListChanged = true, songItemListState.size ${songItemListState.size}, songList.size: ${songList.size}")
//                    Log.i("myDebug", "isSongListChanged = $isSongListChanged, songItemListState = $songItemListState")

            }
        }

         */

    }
}

data class SongItem(
    val song: Song,
    val prevSongImage: Bitmap? = null,
    val currSongImage: Bitmap? = null
)

/*
@Composable
fun LazyColumnComponent(
    lazyColumnState: LazyListState,
    contentPadding: PaddingValues,
    songList: List<Song>,
    onItemClick: (Int) -> Unit,
    onLoadSongImage: (Context, Song) -> MutableStateFlow<Bitmap?>
) {
    Log.i("myDebug", "recompose lazyColumn")
    LazyColumn(
        contentPadding = contentPadding,
        state = lazyColumnState
    ) {
        Log.i("myDebug", "recompose lazyColumn")
        itemsIndexed(songList) { index, song ->
            AudioItem(
                song = song,
                onItemClick = {
                    onItemClick(index)
                },
                onLoadSongImage = onLoadSongImage
            )
        }
    }
}

 */

@Preview(showSystemUi = true)
@Composable
fun SongScreenPrev() {
    SpotifyCloneAndroidAppTheme {
        SongScreen(
            progress = 50f,
            onProgress = {},
            isAudioPlaying = true,
            songListState = remember {
                mutableStateOf(listOf(
                Song("1", "Title One", "Artist 1", "", ""),
                Song("2", "Title Two", "Artist 2", "", ""),
            ))
            },
            currentPlayingAudio = Song("", "Title One", "Artist 1", "", ""),
            onStart = {},
            onItemClick = {},
            onNext = {},
            onLoadSongImage = { _, _ -> MutableStateFlow<Bitmap?>(null) }
        )
    }
}