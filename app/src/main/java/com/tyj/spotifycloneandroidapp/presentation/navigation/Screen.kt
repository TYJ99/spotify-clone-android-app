package com.tyj.spotifycloneandroidapp.presentation.navigation


sealed class Screen(val route: String){
    object Home: Screen("home_screen")
    object PlayingSong: Screen("playing_song_screen")
}