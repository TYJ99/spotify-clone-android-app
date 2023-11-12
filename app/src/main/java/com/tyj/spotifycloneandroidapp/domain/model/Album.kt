package com.tyj.spotifycloneandroidapp.domain.model

data class Album(
    val title: String = "",
    val artist: String = "",
    val imageUrl: String = "",
    val songs: List<Song> = listOf<Song>(),
)
