package com.tyj.spotifycloneandroidapp.domain.exoplayer.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpotifyMusicServiceHandler @Inject constructor(
    private val mediaSession: MediaSession,
) : Player.Listener {
    private val _songState: MutableStateFlow<SongState> = MutableStateFlow(SongState.Initial)
    val songState: StateFlow<SongState> = _songState.asStateFlow()

    private var job: Job? = null

    private val exoPlayer = mediaSession.player

    init {
        exoPlayer.addListener(this)
    }


    fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    // track the event
    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedSongIndex: Int = -1,
        seekPosition: Long = 0,
        isClickSong: Boolean = true,
        traditionalPlayerToggle: Boolean = false,
        getBackFromSongScreen: Boolean = false,
        toggleFromTraditionalPlayer: Boolean = false,
    ) {
        when (playerEvent) {
            PlayerEvent.Backward -> exoPlayer.seekBack()
            PlayerEvent.Forward -> exoPlayer.seekForward()
            PlayerEvent.SeekToNext -> {
                if(!exoPlayer.isPlaying) exoPlayer.playWhenReady = true
                exoPlayer.seekToNext()
            }
            PlayerEvent.SeekToPrevious -> {
                if(!exoPlayer.isPlaying) exoPlayer.playWhenReady = true
                exoPlayer.seekToPrevious()
            }
            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            PlayerEvent.SelectedSongChange -> {
                Log.i("myDebugPager", "exoPlayer.currentMediaItemIndex: ${exoPlayer.currentMediaItemIndex}")
                Log.i("myDebugPager", "selectedSongIndex: $selectedSongIndex")
                Log.i("myDebugPager", "isClickSong: $isClickSong")
                Log.i("myDebugPager", "traditionalPlayerToggle: $traditionalPlayerToggle")

                if((getBackFromSongScreen && !traditionalPlayerToggle) || toggleFromTraditionalPlayer) {
                    return
                }
                else if(!isClickSong && !traditionalPlayerToggle) {
                    exoPlayer.seekToDefaultPosition(selectedSongIndex)
                    _songState.value = SongState.Playing(
                        isPlaying = true
                    )
                    exoPlayer.playWhenReady = true
                    startProgressUpdate()
                }
                else if(isClickSong) {
                    when (selectedSongIndex) {
                        exoPlayer.currentMediaItemIndex -> {
                            playOrPause()
                        }

                        else -> {
                            if(traditionalPlayerToggle) {
                                exoPlayer.seekToDefaultPosition(selectedSongIndex)
                                _songState.value = SongState.Playing(
                                    isPlaying = true
                                )
                                exoPlayer.playWhenReady = true
                                startProgressUpdate()
                            } else {
                                _songState.value = SongState.CurrentPlaying(selectedSongIndex)
                            }
                        }
                    }
                }

            }

            PlayerEvent.Stop -> {
                Log.i("myDebug", "PlayerEvent.Stop")
                stopProgressUpdate()
                //stopSpotifyMusicServiceAndPlayingSong()
            }
            is PlayerEvent.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _songState.value =
                SongState.Buffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> _songState.value =
                SongState.Ready(exoPlayer.duration)


        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _songState.value = SongState.Playing(isPlaying = isPlaying)
        Log.i("myDebugPager", "onIsPlayingChanged, _songState.value: ${_songState.value}")
        _songState.value = SongState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
        Log.i("myDebugPager", "onIsPlayingChanged, _songState.value: ${_songState.value}")
        if (isPlaying) {
            CoroutineScope(Dispatchers.Main).launch {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            Log.i("myDebugPager", "in playOrPause, isPlaying: ${exoPlayer.isPlaying}")
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            Log.i("myDebugPager", "in playOrPause, isPlaying: ${exoPlayer.isPlaying}")
            exoPlayer.play()
            delay(100L)
            _songState.value = SongState.Playing(
                isPlaying = true
            )
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _songState.value = SongState.Progress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        Log.i("myDebug", "stopProgressUpdate")
        job?.cancel()
        _songState.value = SongState.Playing(isPlaying = false)
    }

    /*
    fun stopSpotifyMusicServiceAndPlayingSong() {
        Log.i("myDebug", "stopSpotifyMusicServiceAndPlayingSong ")
        mediaSession.apply {
            release()
            if(player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
                player.release()
            }
        }
    }

     */
}

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object SelectedSongChange : PlayerEvent()
    object Backward : PlayerEvent()
    object SeekToNext : PlayerEvent()
    object SeekToPrevious : PlayerEvent()
    object Forward : PlayerEvent()
    object SeekTo : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class SongState {
    object Initial : SongState()
    data class Ready(val duration: Long) : SongState()
    data class Progress(val progress: Long) : SongState()
    data class Buffering(val progress: Long) : SongState()
    data class Playing(val isPlaying: Boolean) : SongState()
    data class CurrentPlaying(val mediaItemIndex: Int) : SongState()
}