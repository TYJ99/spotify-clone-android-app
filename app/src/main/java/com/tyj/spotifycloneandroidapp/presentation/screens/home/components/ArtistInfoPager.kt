package com.tyj.spotifycloneandroidapp.presentation.screens.home.components

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tyj.spotifycloneandroidapp.domain.model.Song
import kotlinx.coroutines.launch

//https://medium.com/@domen.lanisnik/exploring-the-official-pager-in-compose-8c2698c49a98
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistInfoPager(
    songList: List<Song>,
    mediaIdBitmapMap: Map<String, Bitmap?>,
    loadImage: (Context, Song) -> Unit,
    currPlayingSong: Song,
    onPagerSwipe: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    var currPlayingSongPage by remember { mutableStateOf(0) }

    LaunchedEffect(currPlayingSong) {
        currPlayingSongPage = songList.indexOfFirst { song->
            song.mediaId == currPlayingSong.mediaId
        }
        //Log.i("myDebugPager", "In pager, songListItem: ${songList[currPlayingSongPage]}")
        //Log.i("myDebugPager", "In pager, currPlayingSongPage: $currPlayingSongPage")
    }

    HorizontalPager(
        pageCount = songList.size,
        state = pagerState,
        key = { songList[it].mediaId },
        pageSize = PageSize.Fill,
        modifier = modifier,

    ) { page ->
        val song = songList[page]
        val songImage = mediaIdBitmapMap[song.mediaId]
        if(songImage == null) {
            loadImage(LocalContext.current, song)
        }

        ArtistInfo(
            songImage = songImage,
            song = song,
            onClick = {
                onClick()
            },
        )
    }

    LaunchedEffect(currPlayingSongPage) {
        scope.launch {
            Log.i("myDebugPager", "LaunchedEffect(currPlayingSongPage). currPlayingSongPage: $currPlayingSongPage")
            if(currPlayingSongPage != -1) pagerState.animateScrollToPage(currPlayingSongPage)
            else pagerState.animateScrollToPage(0)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.targetPage }.collect { page ->
            scope.launch {
                Log.i("myDebugPager", "LaunchedEffect(pagerState). current Page: $page")
                Log.i("myDebugPager", "LaunchedEffect(pagerState). currPlayingSong: $currPlayingSong")
                Log.i("myDebugPager", "LaunchedEffect(pagerState). currPlayingSongPage: $currPlayingSongPage")

                if(currPlayingSongPage != -1) {
                    onPagerSwipe(page)
                }

            }
        }
    }
}