package com.tyj.spotifycloneandroidapp.presentation.screens.song

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tyj.spotifycloneandroidapp.R
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
//    var currentSelectedSong by savedStateHandle.saveable { mutableStateOf(dummySong) }
//    var songList by savedStateHandle.saveable { mutableStateOf(listOf<Song>()) }

    private val _currentSelectedSong: MutableStateFlow<Song> = MutableStateFlow(dummySong)
    val currentSelectedSong: StateFlow<Song> = _currentSelectedSong.asStateFlow()

    private val _songList: MutableStateFlow<List<Song>> = MutableStateFlow(listOf<Song>())
    val songList: StateFlow<List<Song>> = _songList.asStateFlow()

    private val _traditionalPlayerToggle: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val traditionalPlayerToggle: StateFlow<Boolean> = _traditionalPlayerToggle.asStateFlow()

    /*
    private val _songImage: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val songImage: StateFlow<Bitmap?> = _songImage.asStateFlow()

     */

    private val mediaIdBitmapItemMap: MutableMap<String, BitMapItem> = mutableMapOf()
    var loadHolderBitmap: Bitmap? = null

//    var duration by mutableStateOf (
//        savedStateHandle.get<Long>("duration") ?: 0L
//    )
//    var progress by mutableStateOf (
//            savedStateHandle.get<Float>("progress") ?: 0f
//    )
//    var progressString by mutableStateOf (
//        savedStateHandle.get<String>("progress") ?: "00:00"
//    )
//    var isPlaying by mutableStateOf (
//        savedStateHandle.get<Boolean>("isPlaying") ?: false
//    )
//    var currentSelectedSong by mutableStateOf (
//        savedStateHandle.get<Song>("currentSelectedSong") ?: dummySong
//    )
//    var songList by mutableStateOf (
//        savedStateHandle.get<Song>("currentSelectedSong") ?: dummySong
//    )

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        Log.i("myDebug", "before loadAudioData: $songList")
        Log.i("myDebug", "loadAudioData")
        loadAudioData()

    }

    init {
        viewModelScope.launch {
            musicServiceHandler.songState.collectLatest { songState ->
                //Log.i("myDebugPager","In SongViewModel Collect, songState: $songState")
                when (songState) {
                    SongState.Initial -> {
                        _uiState.value = UIState.Initial
                    }
                    is SongState.Buffering -> calculateProgressValue(songState.progress)
                    is SongState.Playing -> isPlaying = songState.isPlaying
                    is SongState.Progress -> calculateProgressValue(songState.progress)
                    is SongState.CurrentPlaying -> {
                        _currentSelectedSong.value = _songList.value[songState.mediaItemIndex]
                        //Log.i("myDebugPager", "SongState.CurrentPlaying. _currentSelectedSong: ${_currentSelectedSong.value}")
                    }

                    is SongState.Ready -> {
                        duration = songState.duration
                        _uiState.value = UIState.Ready
                        // if(_songList.value.isNotEmpty()) _currentSelectedSong.value = _songList.value[0]
                    }
                }
            }


        }
    }

    private fun loadAudioData() {
        viewModelScope.launch {
            val audio = repository.fetchMediaData()
            Log.i("myDebug", "firebase data: $audio")
            _songList.value = audio
            Log.i("myDebug", "after loadAudioData: ${_songList.value}")
            setMediaItems()
        }
    }

    private fun setMediaItems() = _songList.value.map { song ->
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

    fun onTraditionalPlayerToggle(toggleOn: Boolean) {
        _traditionalPlayerToggle.value = toggleOn
    }

    fun loadSongImage(context: Context, song: Song): StateFlow<Bitmap?> {
        val bitMapItem = mediaIdBitmapItemMap[song.mediaId]
        bitMapItem?.let { bitmapItem ->
            Log.i("myDebug", "loadSongImage, mediaIdBitmapItemMap: $mediaIdBitmapItemMap")
            bitmapItem.bitmap?.let { bitmap ->
                if(bitmap != loadHolderBitmap) {
                    Log.i("myDebugGlide", "4")
                    Log.i("myDebug", "loadSongImage reduce time, song.mediaId: ${song.mediaId}")
                    Log.i("myDebug", "loadSongImage reduce time, loadHolderBitmap: $loadHolderBitmap")
                    return bitmapItem.bitmapStateFlow.asStateFlow()
                }
            }
        }

        if(bitMapItem == null) {
            mediaIdBitmapItemMap[song.mediaId] = BitMapItem(null, MutableStateFlow(null))
            // first load local default image
            Glide.with(context)
                .asBitmap()
                .load(R.drawable.load_holder)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?) {
                        Log.i("myDebugGlide", "1")
                        Log.i("myDebug", "In Glide, loadSongImage: load_holder, song: ${song.mediaId}")

                        // _songImage.value = resource
                        mediaIdBitmapItemMap[song.mediaId]!!.bitmap = resource
                        mediaIdBitmapItemMap[song.mediaId]!!.bitmapStateFlow.value = resource
                        if(loadHolderBitmap == null) loadHolderBitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                })

            try {
                Glide.with(context)
                    .asBitmap()
                    .load(song.imageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            Log.i("myDebugGlide", "2")
                            Log.i("myDebug", "In Glide, loadSongImage: song.imageUrl, song: ${song.mediaId}")
                            // _songImage.value = resource
                            mediaIdBitmapItemMap[song.mediaId]!!.bitmap = resource
                            mediaIdBitmapItemMap[song.mediaId]!!.bitmapStateFlow.value = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            } catch (glideException: GlideException) {
                Log.i("myDebug", "error: ${glideException.rootCauses}")
            }
        }else {
            if(bitMapItem.bitmap == loadHolderBitmap) {
                // load image from URL
                try {
                    Glide.with(context)
                        .asBitmap()
                        .load(song.imageUrl)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                Log.i("myDebugGlide", "3")
                                Log.i("myDebug", "In Glide, loadSongImage: song.imageUrl, song: ${song.mediaId}")
                                // _songImage.value = resource
                                mediaIdBitmapItemMap[song.mediaId]!!.bitmap = resource
                                mediaIdBitmapItemMap[song.mediaId]!!.bitmapStateFlow.value = resource
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                } catch (glideException: GlideException) {
                    Log.i("myDebug", "glide error: ${glideException.rootCauses}")
                }
            }

        }

        return mediaIdBitmapItemMap[song.mediaId]!!.bitmapStateFlow.asStateFlow()
    }

    fun onUiEvents(
        uiEvents: UIEvents,
        isClickSong: Boolean = true,
        traditionalPlayerToggle: Boolean = false,
    ) = viewModelScope.launch {
        when (uiEvents) {
            UIEvents.Backward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.SeekToNext -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            }
            UIEvents.SeekToPrevious -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.SeekToPrevious)
            }
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
                    playerEvent = PlayerEvent.SelectedSongChange,
                    selectedSongIndex = uiEvents.index,
                    isClickSong = isClickSong,
                    traditionalPlayerToggle = traditionalPlayerToggle,
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
        super.onCleared()
        Log.i("myDebug", "ViewModel onCleared")
//        GlobalScope.launch(Main) {
//            musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
//        }
    }


}

data class BitMapItem(
    var bitmap: Bitmap? = null,
    var bitmapStateFlow: MutableStateFlow<Bitmap?>
)


sealed class UIEvents {
    object PlayPause : UIEvents()
    data class SelectedSongChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object SeekToPrevious : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}