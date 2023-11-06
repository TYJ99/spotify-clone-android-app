package com.tyj.spotifycloneandroidapp.presentation.screens.song.components

import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tyj.spotifycloneandroidapp.common.TopBarBackButton

@Composable
fun SongScreenTopBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Spotify Clone")
        },
        navigationIcon = {
            TopBarBackButton(onBackPressed)
        }
    )
}