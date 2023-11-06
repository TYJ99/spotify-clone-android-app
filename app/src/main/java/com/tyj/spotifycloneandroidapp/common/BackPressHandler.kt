package com.tyj.spotifycloneandroidapp.common

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit,
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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