package com.tyj.spotifycloneandroidapp.presentation.common.components

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit,
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    var enabled by remember { mutableStateOf(true) }

    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                enabled = false
                Log.i("myDebugPressBackButton", "BackPressHandler: backCallback: handleOnBackPressed")
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        Log.i("myDebugPressBackButton", "BackPressHandler: add Callback: $backCallback")
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            Log.i("myDebugPressBackButton", "BackPressHandler: remove Callback: $backCallback")
            backCallback.remove()
        }
    }
}