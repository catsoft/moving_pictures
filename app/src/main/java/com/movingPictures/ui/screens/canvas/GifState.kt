package com.movingPictures.ui.screens.canvas

import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GifState(val gifComposer: GiftComposer) {
    private val scope = CoroutineScope(Dispatchers.Default)

    val previousFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)
    val currentFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)

    fun selectLastFrame() {
        scope.launch {
            gifComposer.frames.collectLatest {
                val index = it.size - 1
                previousFrame.value = it.getOrNull(index - 1) ?: if (it.size > 1) it.lastOrNull() else null
                currentFrame.value = it.getOrNull(index)
            }
        }
    }

    fun selectFrame(frameId: String) {
        scope.launch {
            gifComposer.frames.collectLatest {
                val index = it.indexOfFirst { it.id == frameId }
                previousFrame.value = it.getOrNull(index - 1) ?: if (it.size > 1) it.lastOrNull() else null
                currentFrame.value = it.getOrNull(index)
            }
        }
    }
}