package com.tyj.spotifycloneandroidapp.presentation.screens.song

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.tyj.spotifycloneandroidapp.data.repository.FirebaseMusicRepository
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.PlayerEvent
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SongState
import com.tyj.spotifycloneandroidapp.domain.exoplayer.service.SpotifyMusicServiceHandler
import com.tyj.spotifycloneandroidapp.domain.model.Song
import com.tyj.spotifycloneandroidapp.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


private val dummySong = Song(
    "", "", "", "", ""
)

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class SongViewModel @Inject constructor(
    private val musicServiceHandler: SpotifyMusicServiceHandler,
    private val repository: MusicRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSelectedSong by savedStateHandle.saveable { mutableStateOf(dummySong) }
    var songList by savedStateHandle.saveable { mutableStateOf(listOf<Song>()) }

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        Log.i("viewModel", "loadAudioData")
        loadAudioData()
    }

    init {
        viewModelScope.launch {
            musicServiceHandler.songState.collectLatest { songState ->
                when (songState) {
                    SongState.Initial -> _uiState.value = UIState.Initial
                    is SongState.Buffering -> calculateProgressValue(songState.progress)
                    is SongState.Playing -> isPlaying = songState.isPlaying
                    is SongState.Progress -> calculateProgressValue(songState.progress)
                    is SongState.CurrentPlaying -> {
                        currentSelectedSong = songList[songState.mediaItemIndex]
                    }

                    is SongState.Ready -> {
                        duration = songState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }


        }
    }

    private fun loadAudioData() {
        viewModelScope.launch {
            val audio = repository.fetchMediaData()
            Log.i("firebase data", "$audio")
            songList = audio
            setMediaItems()
        }
    }

    private fun setMediaItems() = songList.map { song ->
        val mediaMetadata = MediaMetadata.Builder()
            .setArtworkUri(song.imageUrl.toUri())
            .setTitle(song.title)
            .setDisplayTitle(song.title)
            .setSubtitle(song.subtitle)
            .setArtist(song.subtitle)
            .setAlbumArtist(song.subtitle)
            .setIsPlayable(true)
            .build()
        /*
        Request metadata. New in (1.0.0-beta01)
        This is optional. I'm adding a RequestMetadata to the MediaItem so I
        can get the mediaUri from my `onAddMediaItems` simple use case (see
        onAddMediaItems for more info).
        If you are going to get the final URI from a database, you can move your
        query to your `MediaLibrarySession.Callback#onAddMediaItems` and skip this.

         */
        /*
        val rmd = MediaItem.RequestMetadata.Builder()
            .setMediaUri("...".toUri())
            .build()

         */

        MediaItem.Builder()
            .setMediaId(song.mediaId)
            .setUri(song.songUrl)
            .setMediaMetadata(mediaMetadata)
            .build()
    }.also { mediaItems ->
        musicServiceHandler.setMediaItemList(mediaItems)
    }

    /*
    private fun setMediaItems() {
        songList.map { audio ->
            MediaItem.Builder()
                .setUri(audio.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audio.artist)
                        .setDisplayTitle(audio.title)
                        .setSubtitle(audio.displayName)
                        .build()
                )
                .build()
        }.also {
            musicServiceHandler.setMediaItemList(it)
        }
    }

     */

    private fun calculateProgressValue(currentProgress: Long) {
        progress =
            if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
            else 0f
        progressString = formatDuration(currentProgress)
    }

    fun onUiEvents(uiEvents: UIEvents) = viewModelScope.launch {
        when (uiEvents) {
            UIEvents.Backward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.SeekToNext -> musicServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvents.PlayPause -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.PlayPause
                )
            }

            is UIEvents.SeekTo -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = ((duration * uiEvents.position) / 100f).toLong()
                )
            }

            is UIEvents.SelectedSongChange -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedSongChange,
                    selectedSongIndex = uiEvents.index
                )
            }

            is UIEvents.UpdateProgress -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvents.newProgress
                    )
                )
                progress = uiEvents.newProgress
            }
        }
    }


    fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format("%02d:%02d", minute, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }


}


sealed class UIEvents {
    object PlayPause : UIEvents()
    data class SelectedSongChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}