package com.tyj.spotifycloneandroidapp.domain.repository

interface MusicRepository {

    suspend fun fetchMediaData(): Unit

}