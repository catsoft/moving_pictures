package com.movingPictures.ui.screens.canvas

import android.util.Log
import com.movingPictures.data.GiftComposer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

enum class PlayerState {
    NOT_READY,
    READY,
}

enum class PlayState {
    PLAYING,
    PAUSED
}

//todo this one should be optimized, it should compile everything into bitmap and then play it
class GifPlayer(private val gif: GiftComposer, private val gifState: GifState) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var playJob: Job? = null

    init {
        scope.launch {
            gif.frames.collectLatest {
                playerState.value = if (it.size > 1) PlayerState.READY else PlayerState.NOT_READY
            }
        }
    }

    val playerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.NOT_READY)
    val playState = MutableStateFlow(PlayState.PAUSED)

    fun play() {
        playJob?.cancel()
        playState.value = PlayState.PLAYING
        playJob = scope.launch {
            gif.frames.collectLatest {
                while (playState.value == PlayState.PLAYING) {
                    for (frame in it) {
                        if (playState.value == PlayState.PAUSED) break
                        gifState.selectFrame(frame.id)
                        Log.i("GifPlayer", "Playing frame ${frame.number}")
                        delay(frame.duration * 1000 / 60)
                    }
                }
            }
        }
    }

    fun pause() {
        playState.value = PlayState.PAUSED
        playJob?.cancel()
    }
}