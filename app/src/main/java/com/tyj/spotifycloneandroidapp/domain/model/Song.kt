package com.tyj.spotifycloneandroidapp.domain.model

import com.google.firebase.firestore.PropertyName

data class Song(
    @PropertyName("mediaId")  var mediaId: String = "",
    @PropertyName("title")    var title: String = "",
    @PropertyName("subtitle") var subtitle: String = "",
    @PropertyName("songUrl")  var songUrl: String = "",
    @PropertyName("imageUrl") var imageUrl: String = "",
)
