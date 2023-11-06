package com.tyj.spotifycloneandroidapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tyj.spotifycloneandroidapp.presentation.screens.home.HomeScreen
import com.tyj.spotifycloneandroidapp.presentation.screens.home.HomeViewModel
import com.tyj.spotifycloneandroidapp.presentation.screens.home.UIEvents
import com.tyj.spotifycloneandroidapp.presentation.screens.song.SongScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    startService: () -> Unit,
) {

    val songListState = homeViewModel.songList.collectAsStateWithLifecycle()
    val currentSelectedSong by homeViewModel.currentSelectedSong.collectAsStateWithLifecycle()
    val toggleState by homeViewModel.traditionalPlayerToggle.collectAsStateWithLifecycle()
    val songDuration = homeViewModel.duration
    //val shouldHandleBackPressed = homeViewModel.shouldHandleBackPressed

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(route = Screen.Home.route){

            HomeScreen(
                progress = homeViewModel.progress,
                onProgress = { homeViewModel.onUiEvents(UIEvents.SeekTo(it)) },
                currPlayingSong = currentSelectedSong,
                isAudioPlaying = homeViewModel.isPlaying,
                songListState = songListState,
                onStart = {
                    homeViewModel.onUiEvents(UIEvents.PlayPause)
                },
                onItemClickOrSwipe = { index, isClickSong, traditionalPlayerToggle ->
                    homeViewModel.onUiEvents(
                        uiEvents =  UIEvents.SelectedSongChange(index),
                        isClickSong = isClickSong,
                        traditionalPlayerToggle = traditionalPlayerToggle,
                    )
                    startService()
                },
                onNext = {
                    homeViewModel.onUiEvents(UIEvents.SeekToNext)
                },
                onPrevious = {
                    homeViewModel.onUiEvents(UIEvents.SeekToPrevious)
                },
                onLoadSongImage = { context, song ->
                    homeViewModel.loadSongImage(context, song)
                },
                toggleState = toggleState,
                onToggle = { toggleState ->
                    homeViewModel.onTraditionalPlayerToggle(toggleState)
                },
                navController = navController,
            )
        }
        composable(route = Screen.PlayingSong.route){
            SongScreen(
                progress = homeViewModel.progress,
                onProgress = { homeViewModel.onUiEvents(UIEvents.SeekTo(it)) },
                currPlayingSong = currentSelectedSong,
                isAudioPlaying = homeViewModel.isPlaying,
                onStart = { homeViewModel.onUiEvents(UIEvents.PlayPause) },
                onNext = {
                    homeViewModel.onUiEvents(UIEvents.SeekToNext)
                },
                onPrevious = {
                    homeViewModel.onUiEvents(UIEvents.SeekToPrevious)
                },
                onLoadSongImage = { context, song ->
                    homeViewModel.loadSongImage(context, song)
                },
                onFastForward = {
                    homeViewModel.onUiEvents(UIEvents.Forward)
                },
                onRewind = {
                    homeViewModel.onUiEvents(UIEvents.Backward)
                },
                navController = navController,
                songDuration = songDuration,
            )
        }
    }
}