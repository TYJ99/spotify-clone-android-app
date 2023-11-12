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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tyj.spotifycloneandroidapp.presentation.common.components.BackInvokeHandler
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.MainActivity
import com.tyj.spotifycloneandroidapp.presentation.navigation.Screen
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
    onItemClickOrSwipe: (Int, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onLoadSongImage: (Context, Song) -> StateFlow<Bitmap?>,
    toggleState: Boolean,
    onToggle: (Boolean) -> Unit,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
//    shouldHandleBackPressed: Boolean,
//    setShouldHandleBackPressed: (Boolean) -> Unit
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

    val shouldHandleBackPressed = navBackStackEntry.savedStateHandle.get<Boolean>("enabled") ?: true
    Log.i("myDebugPressBackButton", "In HomeScreen, before, shouldHandleBackPressed = $shouldHandleBackPressed")

    val pagerPlayer = navBackStackEntry.savedStateHandle.get<Boolean>("pagerPlayer") ?: true

    //Log.i("myDebugPressBackButton", "On Home Screen")
    //Log.i("myDebugPressBackButton", "current, shouldHandleBackPressed = $shouldHandleBackPressed")


    var onBackPressed = {
        Log.i("myDebugPressBackButton", "onBackPressed, activity.moveTaskToBack(true)")
        activity.moveTaskToBack(true)
    }
    if(!shouldHandleBackPressed) onBackPressed = {
        Log.i("myDebugPressBackButton", "onBackPressed: false")
        false
    }

    //val onBackPressed = { activity.onBackPressedDispatcher.onBackPressed() }

    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
        BackInvokeHandler(
            handleBackHandler = true,
            onBackPressed = {
                onBackPressed()
            }
        )
        if(!shouldHandleBackPressed) {
            Log.i("myDebugPressBackButton", "before, shouldHandleBackPressed = ${navBackStackEntry.savedStateHandle.get<Boolean>("enabled")!!}")

            navBackStackEntry.savedStateHandle.set<Boolean>("enabled", true)

            Log.i("myDebugPressBackButton", "after, shouldHandleBackPressed = ${navBackStackEntry.savedStateHandle.get<Boolean>("enabled")!!}")
        }
    }
    else {
        //Log.i("myDebugPressBackButton", "BackHandler, shouldHandleBackPressed = $shouldHandleBackPressed")
        BackHandler(enabled = true) {
            Log.i("myDebugPressBackButton", "On Home Screen")
            Log.i("myDebugPressBackButton", "handle back pressed")

            onBackPressed()
        }

        if(!shouldHandleBackPressed) {
            Log.i("myDebugPressBackButton", "On Home Screen")
            Log.i("myDebugPressBackButton", "before, shouldHandleBackPressed = ${navBackStackEntry.savedStateHandle.get<Boolean>("enabled")!!}")

            navBackStackEntry.savedStateHandle.set<Boolean>("enabled", true)

            Log.i("myDebugPressBackButton", "after, shouldHandleBackPressed = ${navBackStackEntry.savedStateHandle.get<Boolean>("enabled")!!}")
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
            var toggleFromTraditionalPlayer = false
            if(!pagerPlayer && !toggleState) {
                toggleFromTraditionalPlayer = true
            }

            var backFromSongScreen = navBackStackEntry.savedStateHandle.get<Boolean>("getBackFromSongScreen") ?: false
            Log.i("myDebugPager", "In HomeScreen, before, backFromSongScreen: $backFromSongScreen")

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
                    backFromSongScreen = navBackStackEntry.savedStateHandle.get<Boolean>("getBackFromSongScreen") ?: false
                    Log.i("myDebugPager", "In BottomBarPlayer, backFromSongScreen: $backFromSongScreen")
                    onItemClickOrSwipe(
                        page,
                        false,
                        toggleState,
                        backFromSongScreen,
                        toggleFromTraditionalPlayer,
                    )
                    if(backFromSongScreen) {
                        Log.i("myDebugPager", "onPagerSwipe, before, backFromSongScreen: ${navBackStackEntry.savedStateHandle.get<Boolean>("getBackFromSongScreen")}")

                        navBackStackEntry.savedStateHandle["getBackFromSongScreen"] = false
                        Log.i("myDebugPager", "onPagerSwipe, after, backFromSongScreen: ${navBackStackEntry.savedStateHandle.get<Boolean>("getBackFromSongScreen")}")

                    }
                },
            )
            navBackStackEntry.savedStateHandle["pagerPlayer"] = !toggleState
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
                        onItemClickOrSwipe(index, true, toggleState, false, false)
                    },
                    onLoadSongImage = onLoadSongImage,
                    currPlayingSong = currPlayingSong,
                    isAudioPlaying = isAudioPlaying,
                )

            }
        }

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
        var entry: NavBackStackEntry? = null
        NavHost(
            navController = rememberNavController(),
            startDestination = Screen.Home.route,
        ) {
            composable(route = Screen.Home.route) {
                entry = it
            }
        }

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
            onItemClickOrSwipe = {_,_,_,_,_ -> Unit},
            onNext = {},
            onPrevious = {},
            onLoadSongImage = { _, _ -> MutableStateFlow<Bitmap?>(null) },
            toggleState = false,
            onToggle = { _ -> Unit },
            navController = rememberNavController(),
            navBackStackEntry = entry!!,
//            shouldHandleBackPressed = true,
//            setShouldHandleBackPressed = {_ -> Unit},
        )
    }
}