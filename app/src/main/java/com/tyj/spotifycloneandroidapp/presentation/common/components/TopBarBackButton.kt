package com.tyj.spotifycloneandroidapp.presentation.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun TopBarBackButton(onBackPressed: () -> Unit) {

    IconButton(
        onClick = {
            onBackPressed()
        }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back button on top bar"
        )
    }
}