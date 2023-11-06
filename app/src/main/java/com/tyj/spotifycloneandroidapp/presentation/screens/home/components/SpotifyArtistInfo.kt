package com.tyj.spotifycloneandroidapp.presentation.screens.home.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.presentation.navigation.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpotifyArtistInfo(
    modifier: Modifier = Modifier,
    currPlayingSong: Song,
    songList: List<Song>,
    traditionalPlayerToggle: Boolean,
    onPagerSwipe: (Int) -> Unit,
    navController: NavHostController,
) {
    // val pagerState = rememberPagerState()
    var mediaIdBitmapMap: Map<String, Bitmap?> by remember { mutableStateOf(mapOf()) }

    fun loadImage(context: Context, song: Song) {
        Glide.with(context)
            .asBitmap()
            .load(Icons.Default.MusicNote)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    mediaIdBitmapMap = mediaIdBitmapMap.toMutableMap().apply {
                        put(song.mediaId, resource)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        try {
            Glide.with(context)
                .asBitmap()
                .load(song.imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.i("myDebug", "ArtistInfo Glide, load imageUrl")
                        mediaIdBitmapMap = mediaIdBitmapMap.toMutableMap().apply {
                            put(song.mediaId, resource)
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } catch (glideException: GlideException) {
            Log.i("myDebug", "error: ${glideException.rootCauses}")
        }
    }

    fun navigateToTargetScreen() {
        navController.navigate(Screen.PlayingSong.route)
    }

    val song = if(currPlayingSong.mediaId == "" && songList.isNotEmpty()){
        songList[0]
    } else {
        currPlayingSong
    }

    if(!traditionalPlayerToggle) {
        ArtistInfoPager(
            songList = songList,
            mediaIdBitmapMap = mediaIdBitmapMap,
            loadImage = { context, theSong ->
                loadImage(context, theSong)
            },
            currPlayingSong = currPlayingSong,
            onPagerSwipe = onPagerSwipe,
            modifier = modifier,
            onClick = {
                navigateToTargetScreen()
            },
        )
    }else {
        val songImage = mediaIdBitmapMap[song.mediaId]

        if(songImage == null) {
            loadImage(LocalContext.current, currPlayingSong)
        }
        Log.i("myDebug", "traditional Player. currPlayingSong: $song")
        ArtistInfo(
            songImage = songImage,
            song = song,
            modifier = modifier,
            onClick = {
                navigateToTargetScreen()
            },
        )
    }


    /*

    HorizontalPager(
        pageCount =songList.size,
        state = pagerState,
        key = { songList[it].mediaId },
        pageSize = PageSize.Fill,
        modifier = modifier

    ) { index ->
        val song = songList[index]
        val songImage = mediaIdBitmapMap[song.mediaId]
        if(songImage == null) {
            loadImage(LocalContext.current, song)
        }
        Row(
            modifier = modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerImageItem(
                image = songImage,
                icon = Icons.Default.MusicNote,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            ) {}
            Spacer(modifier = Modifier.size(4.dp))
            Column {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = song.subtitle,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
            }
        }

    }

     */

    /*
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerIconItem(
            icon = Icons.Default.MusicNote,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        ) {}
        Spacer(modifier = Modifier.size(4.dp))
        Column {
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Clip,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = song.subtitle,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
    }

     */
}
