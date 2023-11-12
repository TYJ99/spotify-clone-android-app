package com.tyj.spotifycloneandroidapp.data.remote

import com.tyj.spotifycloneandroidapp.common.Constants.SONG_IMAGE_URL
import com.tyj.spotifycloneandroidapp.common.Constants.SONG_URL
import com.tyj.spotifycloneandroidapp.domain.model.Album
import com.tyj.spotifycloneandroidapp.domain.model.Song
import javax.inject.Inject

class FakeMusicDatabase @Inject constructor(): MusicDatabase {
    private val songs = listOf<Song>(
        Song("0", "title0", "subtitle0", SONG_URL, SONG_IMAGE_URL),
        Song("1", "title1", "subtitle1", SONG_URL, SONG_IMAGE_URL),
        Song("2", "title2", "subtitle2", SONG_URL, SONG_IMAGE_URL),
        Song("3", "title3", "subtitle3", SONG_URL, SONG_IMAGE_URL),
        Song("4", "title4", "subtitle4", SONG_URL, SONG_IMAGE_URL),
        Song("5", "title5", "subtitle5", SONG_URL, SONG_IMAGE_URL),
        Song("6", "title6", "subtitle6", SONG_URL, SONG_IMAGE_URL),
        Song("7", "title7", "subtitle7", SONG_URL, SONG_IMAGE_URL),
        Song("8", "title8", "subtitle8", SONG_URL, SONG_IMAGE_URL),
        Song("9", "title9", "subtitle9", SONG_URL, SONG_IMAGE_URL),
        Song("10", "title10", "subtitle10", SONG_URL, SONG_IMAGE_URL),
        Song("11", "title11", "subtitle11", SONG_URL, SONG_IMAGE_URL),
        Song("12", "title12", "subtitle12", SONG_URL, SONG_IMAGE_URL),
        Song("13", "title13", "subtitle13", SONG_URL, SONG_IMAGE_URL),
        Song("14", "title14", "subtitle14", SONG_URL, SONG_IMAGE_URL),
        Song("15", "title15", "subtitle15", SONG_URL, SONG_IMAGE_URL),
        Song("16", "title16", "subtitle16", SONG_URL, SONG_IMAGE_URL),
        Song("17", "title17", "subtitle17", SONG_URL, SONG_IMAGE_URL),
        Song("18", "title18", "subtitle18", SONG_URL, SONG_IMAGE_URL),
        Song("19", "title19", "subtitle19", SONG_URL, SONG_IMAGE_URL),
        Song("20", "title20", "subtitle20", SONG_URL, SONG_IMAGE_URL),
        Song("21", "title21", "subtitle21", SONG_URL, SONG_IMAGE_URL),
        Song("22", "title22", "subtitle22", SONG_URL, SONG_IMAGE_URL),
        Song("23", "title23", "subtitle23", SONG_URL, SONG_IMAGE_URL),
        Song("24", "title24", "subtitle24", SONG_URL, SONG_IMAGE_URL),
        Song("25", "title25", "subtitle25", SONG_URL, SONG_IMAGE_URL),
        Song("26", "title26", "subtitle26", SONG_URL, SONG_IMAGE_URL),
        Song("27", "title27", "subtitle27", SONG_URL, SONG_IMAGE_URL),
        Song("28", "title28", "subtitle28", SONG_URL, SONG_IMAGE_URL),
        Song("29", "title29", "subtitle29", SONG_URL, SONG_IMAGE_URL),
        Song("30", "title30", "subtitle30", SONG_URL, SONG_IMAGE_URL),
        Song("31", "title31", "subtitle31", SONG_URL, SONG_IMAGE_URL),
        Song("32", "title32", "subtitle32", SONG_URL, SONG_IMAGE_URL),
        Song("33", "title33", "subtitle33", SONG_URL, SONG_IMAGE_URL),
        Song("34", "title34", "subtitle34", SONG_URL, SONG_IMAGE_URL),
        Song("35", "title35", "subtitle35", SONG_URL, SONG_IMAGE_URL),
        Song("36", "title36", "subtitle36", SONG_URL, SONG_IMAGE_URL),
        Song("37", "title37", "subtitle37", SONG_URL, SONG_IMAGE_URL),
    )
    override suspend fun getAllSongs(): List<Song> {
        return songs
    }

    override suspend fun getAllAlbums(): List<Album> {
        return listOf()
    }
}