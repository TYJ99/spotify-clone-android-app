package com.tyj.spotifycloneandroidapp.presentation.screens.home

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyj.spotifycloneandroidapp.common.BackInvokeHandler
import com.tyj.spotifycloneandroidapp.common.BackPressHandler
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.MainActivity
import com.tyj.spotifycloneandroidapp.presentation.screens.home.components.AudioItem
import com.tyj.spotifycloneandroidapp.presentation.screens.home.components.BottomBarPlayer
import com.tyj.spotifycloneandroidapp.presentation.screens.home.components.HomeScreenTopBar
import com.tyj.spotifycloneandroidapp.presentation.ui.theme.SpotifyCloneAndroidAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun HomeScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    currPlayingSong: Song,
    isAudioPlaying: Boolean,
    songListState: State<List<Song>>,
    onStart: () -> Unit,
    onItemClickOrSwipe: (Int, Boolean, Boolean) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onLoadSongImage: (Context, Song) -> StateFlow<Bitmap?>,
    toggleState: Boolean,
    onToggle: (Boolean) -> Unit,
    navController: NavHostController,
) {
    val songList by songListState
    val lazyColumnState = rememberLazyListState()
    Log.i("myDebug", "in HomeScreen, songList $songList")
    Log.i("myDebug", "in HomeScreen, songListState ${songListState.value}")
    /*
    LaunchedEffect(key1 = songList) {
        Log.i("myDebug", "LaunchedEffect in SongScreen, songList Changed, songList ${songList.size}")
//        songItemListState = emptyList()
//        songItemMapState = emptyMap()
//        isSongListChanged = true
    }

     */

    val activity = when(LocalLifecycleOwner.current) {
        is MainActivity -> LocalLifecycleOwner.current as MainActivity
        else -> {
            val context = LocalContext.current
            if (context is MainActivity) {
                context
            } else {
                throw IllegalStateException("LocalLifecycleOwner is not MainActivity or Fragment")
            }
        }
    }

    val onBackPressed = { activity.moveTaskToBack(true) }

    //val onBackPressed = { activity.onBackPressedDispatcher.onBackPressed() }

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        BackInvokeHandler(
            handleBackHandler = true,
            onBackPressed = {
                onBackPressed()
            }
        )
    } else {
        BackHandler(enabled = true) {
            Log.i("myDebugPressBackButton", "On Home Screen")
            Log.i("myDebugPressBackButton", "handle back pressed")
            onBackPressed()
        }
    }




//    BackPressHandler(
//        onBackPressed = { onBackPressed() },
//    )

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                toggleState = toggleState,
                onToggle = onToggle,
                onBackPressed = {
                    onBackPressed()
                }
            )
        },
        bottomBar = {
            BottomBarPlayer(
                progress = progress,
                onProgress = onProgress,
                isAudioPlaying = isAudioPlaying,
                onStart = onStart,
                onNext = onNext,
                onPrevious = onPrevious,
                currPlayingSong = currPlayingSong,
                songList = songList,
                traditionalPlayerToggle = toggleState,
                navController = navController,
                onPagerSwipe = { page ->
                    onItemClickOrSwipe(page, false, toggleState)
                },
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
                        onItemClickOrSwipe(index, true, toggleState)
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
        HomeScreen(
            progress = 50f,
            onProgress = {},
            currPlayingSong = Song("1", "Title One", "Artist 1", "", ""),
            isAudioPlaying = true,
            songListState = remember {
                mutableStateOf(listOf(
                Song("1", "Title One", "Artist 1", "", ""),
                Song("2", "Title Two", "Artist 2", "", ""),
            ))
            },
            onStart = {},
            onItemClickOrSwipe = {_,_,_ -> Unit},
            onNext = {},
            onPrevious = {},
            onLoadSongImage = { _, _ -> MutableStateFlow<Bitmap?>(null) },
            toggleState = false,
            onToggle = { _ -> Unit },
            navController = rememberNavController(),
        )
    }
}