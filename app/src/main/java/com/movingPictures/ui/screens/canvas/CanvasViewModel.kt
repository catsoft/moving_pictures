package com.movingPictures.ui.screens.canvas

import com.movingPictures.data.FrameComposer
import com.movingPictures.data.GiftComposer
import com.movingPictures.data.dto.Frame
import kotlinx.coroutines.flow.MutableStateFlow

class CanvasViewModel() {
    val undoButtonState = MutableStateFlow(ControllableState.DISABLED)
    val redoButtonState = MutableStateFlow(ControllableState.DISABLED)

    val playButtonState = MutableStateFlow(ControllableState.DISABLED)
    val pauseButtonState = MutableStateFlow(ControllableState.DISABLED)

    val deleteButtonState = MutableStateFlow(ControllableState.IDLE)
    val addButtonState = MutableStateFlow(ControllableState.IDLE)
    val layersButtonState = MutableStateFlow(ControllableState.IDLE)

    val penButtonState = MutableStateFlow(ControllableState.IDLE)
    val brushButtonState = MutableStateFlow(ControllableState.IDLE)
    val eraserButtonState = MutableStateFlow(ControllableState.IDLE)
    val editButtonState = MutableStateFlow(ControllableState.IDLE)
    val colorButtonState = MutableStateFlow(ControllableState.ACTIVE)


    val gifComposer = GiftComposer()

    val previousFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)
    val currentFrame: MutableStateFlow<FrameComposer?> = MutableStateFlow(null)

    init {
        gifComposer.addFrame(Frame())
        selectFrame(gifComposer.frames.first().id)
    }

    fun selectFrame(frameId: String) {
        val index = gifComposer.frames.indexOfFirst { it.id == frameId }
        previousFrame.value = gifComposer.frames.getOrNull(index - 1) ?: if (gifComposer.frames.size > 1) gifComposer.frames.lastOrNull() else null
        currentFrame.value = gifComposer.frames.getOrNull(index)
    }
}