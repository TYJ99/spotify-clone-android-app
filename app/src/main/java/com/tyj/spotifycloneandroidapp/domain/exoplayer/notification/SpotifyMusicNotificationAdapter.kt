package com.tyj.spotifycloneandroidapp.domain.exoplayer.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tyj.spotifycloneandroidapp.GlideApp
import com.tyj.spotifycloneandroidapp.R


@UnstableApi
class SpotifyMusicNotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?,
) : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence =
        player.mediaMetadata.title ?: "Unknown"

    override fun createCurrentContentIntent(player: Player): PendingIntent? = pendingIntent

    override fun getCurrentContentText(player: Player): CharSequence =
        player.mediaMetadata.subtitle ?: "Unknown"

    private val requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_music)
        .error(R.drawable.ic_music)

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback,
    ): Bitmap? {
        GlideApp.with(context)
            .asBitmap()
            .apply(requestOptions)
            .load(player.mediaMetadata.artworkUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback.onBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
        return null
    }
}