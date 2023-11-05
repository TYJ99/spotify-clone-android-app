package com.tyj.spotifycloneandroidapp.data.remote

import com.tyj.spotifycloneandroidapp.domain.model.Song
import javax.inject.Inject

class FakeMusicDatabase @Inject constructor(): MusicDatabase {
    private val songURL = "https://dl.musopen.org/recordings/0d2b20df-6ba6-46c9-b058-62f5e7976cb0.mp3?filename=Cello+Suite+no.+1+-+Prelude+in+G%2C+BWV+1007.mp3&preview"
    private val songImageURL = "https://source.unsplash.com/user/c_v_r/"
    private val songs = listOf<Song>(
        Song("0", "title0", "subtitle0", songURL, songImageURL),
        Song("1", "title1", "subtitle1", songURL, songImageURL),
        Song("2", "title2", "subtitle2", songURL, songImageURL),
        Song("3", "title3", "subtitle3", songURL, songImageURL),
        Song("4", "title4", "subtitle4", songURL, songImageURL),
        Song("5", "title5", "subtitle5", songURL, songImageURL),
        Song("6", "title6", "subtitle6", songURL, songImageURL),
        Song("7", "title7", "subtitle7", songURL, songImageURL),
        Song("8", "title8", "subtitle8", songURL, songImageURL),
        Song("9", "title9", "subtitle9", songURL, songImageURL),
        Song("10", "title10", "subtitle10", songURL, songImageURL),
        Song("11", "title11", "subtitle11", songURL, songImageURL),
        Song("12", "title12", "subtitle12", songURL, songImageURL),
        Song("13", "title13", "subtitle13", songURL, songImageURL),
        Song("14", "title14", "subtitle14", songURL, songImageURL),
        Song("15", "title15", "subtitle15", songURL, songImageURL),
        Song("16", "title16", "subtitle16", songURL, songImageURL),
        Song("17", "title17", "subtitle17", songURL, songImageURL),
        Song("18", "title18", "subtitle18", songURL, songImageURL),
        Song("19", "title19", "subtitle19", songURL, songImageURL),
        Song("20", "title20", "subtitle20", songURL, songImageURL),
        Song("21", "title21", "subtitle21", songURL, songImageURL),
        Song("22", "title22", "subtitle22", songURL, songImageURL),
        Song("23", "title23", "subtitle23", songURL, songImageURL),
        Song("24", "title24", "subtitle24", songURL, songImageURL),
        Song("25", "title25", "subtitle25", songURL, songImageURL),
        Song("26", "title26", "subtitle26", songURL, songImageURL),
        Song("27", "title27", "subtitle27", songURL, songImageURL),
        Song("28", "title28", "subtitle28", songURL, songImageURL),
        Song("29", "title29", "subtitle29", songURL, songImageURL),
        Song("30", "title30", "subtitle30", songURL, songImageURL),
        Song("31", "title31", "subtitle31", songURL, songImageURL),
        Song("32", "title32", "subtitle32", songURL, songImageURL),
        Song("33", "title33", "subtitle33", songURL, songImageURL),
        Song("34", "title34", "subtitle34", songURL, songImageURL),
        Song("35", "title35", "subtitle35", songURL, songImageURL),
        Song("36", "title36", "subtitle36", songURL, songImageURL),
        Song("37", "title37", "subtitle37", songURL, songImageURL),
    )
    override suspend fun getAllSongs(): List<Song> {
        return songs
    }
}